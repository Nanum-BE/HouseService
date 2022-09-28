package com.nanum.houseservice.house.application;

import com.nanum.config.HouseStatus;
import com.nanum.exception.NotFoundException;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.domain.HouseImg;
import com.nanum.houseservice.house.dto.HouseOptionDto;
import com.nanum.houseservice.house.infrastructure.HouseOptionConnRepository;
import com.nanum.houseservice.house.vo.HouseOptionConnResponse;
import com.nanum.houseservice.option.domain.HouseOption;
import com.nanum.houseservice.house.domain.HouseOptionConn;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.infrastructure.HouseFileRepository;
import com.nanum.houseservice.house.infrastructure.HouseImgRepository;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import com.nanum.houseservice.house.vo.HostHouseResponse;
import com.nanum.houseservice.house.vo.HouseImgResponse;
import com.nanum.houseservice.house.vo.HouseResponse;
import com.nanum.houseservice.option.infrastructure.HouseOptionRepository;
import com.nanum.houseservice.option.vo.HouseOptionResponse;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final S3UploaderService s3UploaderService;
    private final HouseFileRepository houseFileRepository;
    private final HouseImgRepository houseImgRepository;
    private final HouseOptionRepository houseOptionRepository;
    private final HouseOptionConnRepository houseOptionConnRepository;

    @Override
    public void createHouse(HouseDto houseDto, MultipartFile houseMainImg,
                            MultipartFile floorPlanImg, MultipartFile houseFile,
                            List<MultipartFile> houseImgs) {
        houseDto.setStatus(HouseStatus.BEFORE_APPROVAL);
        House house;

        /** 1. 하우스 메인 사진 S3에 저장
            2. 도면 사진 S3에 저장
            3. 하우스 기본 정보 + 하우스 메인 사진 정보 + 도면 사진 정보를 house 테이블에 Insert **/
        try {
            S3UploadDto houseMainImgDto = new S3UploadDto();
            S3UploadDto floorPlanImgDto = new S3UploadDto();

            if(houseMainImg != null && !houseMainImg.isEmpty()) {
                houseMainImgDto = s3UploaderService.upload(List.of(houseMainImg), "houseMain").get(0);
            }
            if(floorPlanImg != null && !floorPlanImg.isEmpty()) {
                floorPlanImgDto = s3UploaderService.upload(List.of(floorPlanImg), "floorPlan").get(0);
            }

            house = houseDto.houseDtoToEntity(houseMainImgDto, floorPlanImgDto);
            house = houseRepository.save(house);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /** 하우스 옵션을 house_conn 테이블에 Insert **/
        if(houseDto.getHouseOption() != null) {
            try {
                for (Long houseOptionId : houseDto.getHouseOption()) {
                    HouseOption houseOption = houseOptionRepository.findByHouseOptionId(houseOptionId);
                    HouseOptionConn houseOptionConn = HouseOptionConn.builder()
                            .house(house)
                            .houseOption(houseOption)
                            .build();
                    houseOptionConnRepository.save(houseOptionConn);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /** 1. 하우스 상세 사진(리스트)을 S3에 저장
            2. 하우스 상세 사진 정보 + 우선순위를 house_img 테이블에 Insert **/
        try {
            if(houseFile != null && !houseFile.isEmpty()) {
                S3UploadDto houseFileDto = s3UploaderService.upload(List.of(houseFile), "houseFile").get(0);
                houseFileRepository.save(houseFileDto.houseFileToEntity(house));
            }

            List<S3UploadDto> houseImgsDto = new ArrayList<>();
            if(houseImgs != null && !houseImgs.isEmpty() && !houseImgs.get(0).isEmpty()) {
                houseImgsDto = s3UploaderService.upload(houseImgs, "house");
            }

            List<HouseImg> imgList = new ArrayList<>();
            for(int i = 0; i < houseImgsDto.size(); i++) {
                imgList.add(houseImgsDto.get(i).houseImgToEntity(house, i+1));
            }

            houseImgRepository.saveAll(imgList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<HostHouseResponse> retrieveHostAllHouses(Long hostId) {

        List<House> houses = houseRepository.findAllByHostId(hostId);

        if(houses.isEmpty()) {
            throw new NotFoundException(String.format("No Lookup Value"));
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return Arrays.asList(mapper.map(houses, HostHouseResponse[].class));
    }

    @Override
    public HouseResponse retrieveHostHouse(Long hostId, Long houseId) {
        House house = houseRepository.findById(houseId).get();
        List<HouseImg> houseImgs = houseImgRepository.findAllByHouseHouseIdOrderByPriorityAsc(houseId);
        List<HouseOptionConn> houseOptionConns = houseOptionConnRepository.findAllByHouseHouseId(houseId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseResponse houseResponse = mapper.map(house, HouseResponse.class);
        List<HouseImgResponse> houseImgResponses = Arrays.asList(mapper.map(houseImgs, HouseImgResponse[].class));
        List<HouseOptionConnResponse> houseOptionConnResponses = new ArrayList<>();

        houseOptionConns.forEach(h -> houseOptionConnResponses.add(HouseOptionConnResponse.builder()
                        .houseOptionConnId(h.getHouseOption().getHouseOptionId())
                        .optionName(h.getHouseOption().getOptionName())
                        .iconPath(h.getHouseOption().getIconPath())
                .build()));

        houseResponse.setHouseImgs(houseImgResponses);
        houseResponse.setHouseOptions(houseOptionConnResponses);

        return houseResponse;
    }
}
