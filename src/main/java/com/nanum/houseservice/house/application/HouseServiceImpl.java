package com.nanum.houseservice.house.application;

import com.nanum.houseservice.house.infrastructure.HouseRepositoryCustom;
import com.nanum.config.HouseStatus;
import com.nanum.exception.CustomRunTimeException;
import com.nanum.exception.NotFoundException;
import com.nanum.houseservice.house.domain.*;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.dto.HouseSearch;
import com.nanum.houseservice.house.dto.HouseSearchDto;
import com.nanum.houseservice.house.dto.PopularHouseDto;
import com.nanum.houseservice.house.infrastructure.*;
import com.nanum.houseservice.house.vo.*;
import com.nanum.houseservice.option.domain.HouseOption;
import com.nanum.houseservice.option.infrastructure.HouseOptionRepository;
import com.nanum.houseservice.option.vo.HouseOptionCheckResponse;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.houseservice.room.infrastructure.RoomRepository;
import com.nanum.houseservice.wish.domain.Wish;
import com.nanum.houseservice.wish.dto.WishIdDto;
import com.nanum.houseservice.wish.infrastructure.WishRepository;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final WishRepository wishRepository;
    private final HouseSearchRepository houseSearchRepository;
    private final HouseSearchQueryRepository houseSearchQueryRepository;
    private final RoomRepository roomRepository;
    private final HouseRepositoryCustom houseRepositoryCustom;

    @Override
    public HouseCreateResponse createHouse(HouseDto houseDto, MultipartFile houseMainImg,
                                           MultipartFile floorPlanImg, MultipartFile houseFile,
                                           List<MultipartFile> houseImgs) {
        houseDto.setStatus(HouseStatus.BEFORE_APPROVAL);
        House house;

        /** 1. ????????? ?????? ?????? S3??? ??????
         2. ?????? ?????? S3??? ??????
         3. ????????? ?????? ?????? + ????????? ?????? ?????? ?????? + ?????? ?????? ????????? house ???????????? Insert **/
        try {
            S3UploadDto houseMainImgDto = new S3UploadDto();
            S3UploadDto floorPlanImgDto = new S3UploadDto();

            if (houseMainImg != null && !houseMainImg.isEmpty()) {
                houseMainImgDto = s3UploaderService.upload(List.of(houseMainImg), "houseMain").get(0);
            }
            if (floorPlanImg != null && !floorPlanImg.isEmpty()) {
                floorPlanImgDto = s3UploaderService.upload(List.of(floorPlanImg), "floorPlan").get(0);
            }

            house = houseDto.houseDtoToEntity(houseMainImgDto, floorPlanImgDto, null);
            house = houseRepository.save(house);

            HouseDocument houseDocument = HouseDocument.from(house);
            houseSearchRepository.save(houseDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /** ????????? ????????? house_conn ???????????? Insert **/
        if (houseDto.getHouseOption() != null) {
            try {
                House finalHouse = house;

                houseDto.getHouseOption().forEach(h -> {
                    if (h.getIsChecked()) {
                        HouseOption houseOption = houseOptionRepository.findById(h.getHouseOptionId()).orElse(null);
                        HouseOptionConn houseOptionConn = HouseOptionConn.builder()
                                .house(finalHouse)
                                .houseOption(houseOption)
                                .build();
                        houseOptionConnRepository.save(houseOptionConn);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /** 1. ????????? ??????, ????????? ?????? ??????(?????????)??? S3??? ??????
         2. ????????? ??????, ????????? ?????? ?????? ????????? house_img ???????????? Insert **/
        try {
            if (houseFile != null && !houseFile.isEmpty()) {
                S3UploadDto houseFileDto = s3UploaderService.upload(List.of(houseFile), "houseFile").get(0);
                houseFileRepository.save(houseFileDto.houseFileToEntity(house));
            }

            List<S3UploadDto> houseImgsDto = new ArrayList<>();
            if (houseImgs != null && !houseImgs.isEmpty() && !houseImgs.get(0).isEmpty()) {
                houseImgsDto = s3UploaderService.upload(houseImgs, "house");
            }

            List<HouseImg> imgList = new ArrayList<>();
            for (S3UploadDto s3UploadDto : houseImgsDto) {
                imgList.add(s3UploadDto.houseImgToEntity(house));
            }

            houseImgRepository.saveAll(imgList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return HouseCreateResponse.builder()
                .id(house.getId())
                .houseName(house.getHouseName())
                .mainHouseImgPath(house.getMainHouseImgPath())
                .build();
    }

    @Override
    public Page<HostHouseResponse> retrieveHostAllHouses(Long hostId, Pageable pageable) {

        Page<House> houses = houseRepository.findAllByHostId(hostId, pageable);

        if (houses.isEmpty()) {
            throw new NotFoundException("No Lookup Value");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return houses.map(house -> {
            return mapper.map(house, HostHouseResponse.class);
        });
    }

    @Override
    public HouseResponse retrieveHouseDetails(Long userId, Long houseId) {
        House house = houseRepository.findById(houseId).get();
        List<HouseImg> houseImgs = houseImgRepository.findAllByHouseId(houseId);
        List<HouseOptionConn> houseOptionConns = houseOptionConnRepository.findAllByHouseId(houseId);

        Wish wish = userId != null ? wishRepository.findByUserIdAndHouse(userId, house) : null;

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseResponse houseResponse = mapper.map(house, HouseResponse.class);
        houseResponse.setHouseMainImg(house.getMainHouseImgPath());
        houseResponse.setFloorPlanImg(house.getFloorPlanPath());

        List<String> keyWord = new ArrayList<>(List.of(house.getKeyWord().split("#")));
        if (keyWord.size() > 1) keyWord.remove(0);
        houseResponse.setKeyWord(keyWord);

        List<HouseImgResponse> houseImgResponses = Arrays.asList(mapper.map(houseImgs, HouseImgResponse[].class));
        List<HouseOptionCheckResponse> houseOptionConnResponses = new ArrayList<>();

        houseOptionConns.forEach(h -> houseOptionConnResponses.add(HouseOptionCheckResponse.builder()
                .houseOptionId(h.getHouseOption().getId())
                .optionName(h.getHouseOption().getOptionName())
                .iconPath(h.getHouseOption().getIconPath())
                .build()));

        houseResponse.setHouseImgs(houseImgResponses);
        houseResponse.setHouseOption(houseOptionConnResponses);
        houseResponse.setWishId(wish != null ? wish.getId() : null);

        return houseResponse;
    }

    @Override
    public HouseOriginResponse retrieveOriginHouse(Long hostId, Long houseId) {
        House house = houseRepository.findById(houseId).get();
        List<HouseImg> houseImgs = houseImgRepository.findAllByHouseId(houseId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseRequest houseRequest = mapper.map(house, HouseRequest.class);

        List<HouseImgResponse> houseImgResponses = Arrays.asList(mapper.map(houseImgs, HouseImgResponse[].class));

        List<HouseOptionConn> houseOptionConns = houseOptionConnRepository.findAllByHouseId(houseId);
        List<Long> houseOptionIds = new ArrayList<>();

        houseOptionConns.forEach(houseOptionConn -> houseOptionIds.add(houseOptionConn.getHouseOption().getId()));

        List<HouseOption> houseOptionList = houseOptionRepository.findAll();
        List<HouseOptionCheckResponse> houseOption = new ArrayList<>();

        houseOptionList.forEach(h -> {
                    boolean isChecked = houseOptionIds.contains(h.getId());
                    houseOption.add(HouseOptionCheckResponse.builder()
                            .houseOptionId(h.getId())
                            .optionName(h.getOptionName())
                            .isChecked(isChecked)
                            .iconPath(h.getIconPath())
                            .build());
                }
        );

        List<String> keyWord = new ArrayList<>(List.of(house.getKeyWord().split("#")));
        if (keyWord.size() > 1) keyWord.remove(0);

        houseRequest.setKeyWord(keyWord);
        houseRequest.setHouseOption(houseOption);

        HouseFile houseFile = houseFileRepository.findByHouseId(houseId);

        return new HouseOriginResponse(houseRequest,
                house.getMainHouseImgPath(),
                house.getFloorPlanPath(),
                houseFile.getFilePath(),
                houseImgResponses);
    }

    @Override
    public void updateHouse(Long houseId, HouseDto houseDto,
                            MultipartFile houseMainImg, MultipartFile floorPlanImg) {

        House house = houseRepository.findById(houseId).get();
        houseDto.setStatus(house.getStatus());

        S3UploadDto houseMainImgDto;
        S3UploadDto floorPlanImgDto;

        try {
            if (houseMainImg != null && !houseMainImg.isEmpty()) {
                houseMainImgDto = s3UploaderService.upload(List.of(houseMainImg), "houseMain").get(0);
            } else {
                houseMainImgDto = S3UploadDto.builder()
                        .originName(house.getMainHouseImgOriginName())
                        .saveName(house.getMainHouseImgSaveName())
                        .imgUrl(house.getMainHouseImgPath())
                        .build();
            }
            if (floorPlanImg != null && !floorPlanImg.isEmpty()) {
                floorPlanImgDto = s3UploaderService.upload(List.of(floorPlanImg), "floorPlan").get(0);
            } else {
                floorPlanImgDto = S3UploadDto.builder()
                        .originName(house.getFloorPlanOriginName())
                        .saveName(house.getFloorPlanSaveName())
                        .imgUrl(house.getFloorPlanPath())
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        House newHouse = houseDto.houseDtoToEntity(houseMainImgDto, floorPlanImgDto, houseId);
        houseRepository.save(newHouse);

        try {
            List<HouseOptionConn> houseOptionConns = houseOptionConnRepository.findAllByHouseId(houseId);

            houseOptionConnRepository.deleteAll(houseOptionConns);

            if (houseDto.getHouseOption().size() > 0) {
                houseDto.getHouseOption().forEach(h -> {
                    if (h.getIsChecked()) {
                        HouseOption houseOption = houseOptionRepository.findById(h.getHouseOptionId()).orElse(null);
                        HouseOptionConn houseOptionConn = HouseOptionConn.builder()
                                .house(house)
                                .houseOption(houseOption)
                                .build();
                        houseOptionConnRepository.save(houseOptionConn);
                    }
                });
            }
        } catch (Exception e) {
            throw new CustomRunTimeException("?????? ?????? ??????");
        }
    }

    @Override
    public void updateHouseImg(Long hostId, Long houseId, List<Long> deleteHouseImgs, List<MultipartFile> houseImgs) {
        if (deleteHouseImgs != null && !deleteHouseImgs.isEmpty()) {
            houseImgRepository.deleteAllById(deleteHouseImgs);
        }

        List<S3UploadDto> houseImgsDto = new ArrayList<>();

        try {
            if (houseImgs != null && !houseImgs.isEmpty() && !houseImgs.get(0).isEmpty()) {
                houseImgsDto = s3UploaderService.upload(houseImgs, "house");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        House house = houseRepository.findById(houseId).get();
        List<HouseImg> imgList = new ArrayList<>();

        for (S3UploadDto s3UploadDto : houseImgsDto) {
            imgList.add(s3UploadDto.houseImgToEntity(house));
        }

        houseImgRepository.saveAll(imgList);
    }

    @Override
    public void updateHouseFile(Long hostId, Long houseId, MultipartFile houseFile) {
        House house = houseRepository.findById(houseId).get();
        HouseFile originHouseFile = houseFileRepository.findById(houseId).get();
        houseFileRepository.delete(originHouseFile);

        try {
            if (houseFile != null && !houseFile.isEmpty()) {
                S3UploadDto houseFileDto = s3UploaderService.upload(List.of(houseFile), "houseFile").get(0);
                houseFileRepository.save(houseFileDto.houseFileToEntity(house));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HouseFileResponse retrieveHouseFile(Long hostId, Long houseId) {
        HouseFile houseFile = houseFileRepository.findByHouseId(houseId);

        if (houseFile == null) {
            throw new NotFoundException("No Lookup Value");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(houseFile, HouseFileResponse.class);
    }

    @Override
    public List<HouseSearchResponse> retrieveHouseSearch(String searchWord, Long userId) {
        List<HouseSearch> houses = searchWord != null ? houseRepository.findAllBySearchWord(searchWord) : houseRepository.findAllBySearch();

        Long[] houseIdList = houses.stream().map(houseSearch -> houseSearch.getHouse().getId()).toArray(Long[]::new);

        List<WishIdDto> wishIdDtoList = userId != null ? wishRepository.findWishId(userId, houseIdList) : new ArrayList<>();

        List<HouseSearchResponse> houseSearchResponses = new ArrayList<>();

        for (HouseSearch hs : houses) {
            WishIdDto wishId = wishIdDtoList.stream()
                    .filter(w -> w.getHouseId().equals(hs.getHouse().getId())).findFirst().orElse(null);
            houseSearchResponses.add(hs.toSearchResponse(wishId != null ? wishId.getWishId() : null));
        }

        return houseSearchResponses;
    }

    @Override
    public List<String> retrieveAutoHouseSearch(String searchWord) {
        return houseSearchQueryRepository.findBySearchWord(searchWord);
    }

    @Override
    public List<HouseElasticSearchResponse> retrieveHouseByElastic(String searchWord) {
        return houseSearchQueryRepository.findByElastic(searchWord);
    }

    @Override
    public List<HouseElasticSearchResponse> retrieveHouseByOption(HouseSearchDto houseSearchDto) {

        Double distance = distance(houseSearchDto.getCenterX(), houseSearchDto.getCenterY(),
                houseSearchDto.getSouthWestX(), houseSearchDto.getSouthWestY());

        houseSearchDto.setDistance(distance);
        return houseSearchQueryRepository.findByOption(houseSearchDto);
    }

    @Override
    public List<HouseElasticSearchResponse> retrieveHouseByRegion(String region) {
        return houseSearchQueryRepository.findByRegion(region);
    }

    @Override
    public List<HouseCountResponse> retrieveHouseCountByRegion() {
        return houseSearchQueryRepository.countByRegion();
    }

    @Override
    public HouseTotalResponse retrieveHouseTotal(Long houseId, Long userId) {
        if (!houseRepository.existsById(houseId)) {
            throw new NotFoundException("????????? ????????? ???????????????.");
        }

        HouseSearch houseSearch = houseRepository.findTotal(houseId);

        if (houseSearch.getHouse() == null) {
            List<Room> rooms = roomRepository.findByHouseId(houseId);
            houseSearch = new HouseSearch(houseRepository.findById(houseId).orElse(null),
                    rooms.stream().mapToInt(Room::getMonthlyRent).max().orElse(0),
                    rooms.stream().mapToInt(Room::getMonthlyRent).min().orElse(0));
        }

        Wish wish = userId != null ? wishRepository.findByUserIdAndHouse(userId, houseSearch.getHouse()) : null;

        return houseSearch.from(wish != null ? wish.getId() : null);
    }

    @Override
    public void createHouseDocument() {
        List<House> houses = houseRepository.findAll();

        List<HouseDocument> houseDocuments = houses.stream()
                .map(HouseDocument::from)
                .collect(Collectors.toList());

        houseSearchRepository.saveAll(houseDocuments);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    public List<PopularHouseDto> retrievePopularHouses() {
        List<House> mainHouses = houseRepository.findTop10ByOrderByUpdateAtDesc();
        return mainHouses.stream()
                .map(PopularHouseDto::of)
                .collect(Collectors.toList());

    }

    @Override
    public List<PopularHouseDto> retrieveMyHouses() {
        List<House> list = houseRepository.findTop10ByHouseTypeOrderByUpdateAtDesc("?????????");
        return list.stream()
                .map(PopularHouseDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<PopularHouseDto> retrieveShareList() {
        return houseRepositoryCustom.findByHouseInfo(10);
    }
}
