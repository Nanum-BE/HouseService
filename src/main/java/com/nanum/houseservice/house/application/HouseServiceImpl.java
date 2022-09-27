package com.nanum.houseservice.house.application;

import com.nanum.config.BaseResponse;
import com.nanum.config.HouseStatus;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.domain.HouseImg;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.infrastructure.HouseFileRepository;
import com.nanum.houseservice.house.infrastructure.HouseImgRepository;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import com.nanum.houseservice.house.vo.HostHouseResponse;
import com.nanum.houseservice.house.vo.HouseResponse;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Host;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final S3UploaderService s3UploaderService;
    private final HouseFileRepository houseFileRepository;
    private final HouseImgRepository houseImgRepository;

    @Override
    public void createHouse(HouseDto houseDto, MultipartFile houseMainImg,
                            MultipartFile floorPlanImg, MultipartFile houseFile,
                            List<MultipartFile> houseImgs) {
        houseDto.setStatus(HouseStatus.BEFORE_APPROVAL);
        House house;

        try {
            List<S3UploadDto> houseMainImgDto = s3UploaderService.upload(List.of(houseMainImg), "houseMain");
            List<S3UploadDto> floorPlanImgDto = s3UploaderService.upload(List.of(floorPlanImg), "floorPlan");

            house = houseDto.houseDtoToEntity(houseMainImgDto.get(0), floorPlanImgDto.get(0));
            houseRepository.save(house);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            List<S3UploadDto> houseFileDto = s3UploaderService.upload(List.of(houseFile), "houseFile");
            houseFileRepository.save(houseFileDto.get(0).houseFileToEntity(house));

            List<S3UploadDto> houseImgsDto = s3UploaderService.upload(houseImgs, "house");
            List<HouseImg> imgList = new ArrayList<>();
            houseImgsDto.forEach(h -> imgList.add(h.houseImgToEntity(house)));

            houseImgRepository.saveAll(imgList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<HostHouseResponse> retrieveHostAllHouses(Long hostId) {

        List<House> houses = houseRepository.findAllByHostId(hostId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return Arrays.asList(mapper.map(houses, HostHouseResponse[].class));
    }

    @Override
    public HouseResponse retrieveHostHouse(Long hostId, Long houseId) {
        House house = houseRepository.findById(houseId).get();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(house, HouseResponse.class);
    }
}
