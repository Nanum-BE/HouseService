package com.nanum.houseservice.house.application;

import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.dto.HouseSearchDto;
import com.nanum.houseservice.house.dto.PopularHouseDto;
import com.nanum.houseservice.house.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HouseService {
    HouseCreateResponse createHouse(HouseDto houseDto, MultipartFile houseMainImg,
                     MultipartFile floorPlanImg, MultipartFile houseFile,
                     List<MultipartFile> houseImgs);

    Page<HostHouseResponse> retrieveHostAllHouses(Long hostId, Pageable pageable);
    HouseResponse retrieveHouseDetails(Long userId, Long houseId);
    HouseOriginResponse retrieveOriginHouse(Long hostId, Long houseId);
    void updateHouse(Long houseId, HouseDto houseDto, MultipartFile houseMainImg, MultipartFile floorPlanImg);
    void updateHouseImg(Long hostId, Long houseId, List<Long> deleteHouseImgs, List<MultipartFile> houseImgs);
    void updateHouseFile(Long hostId, Long houseId, MultipartFile houseFile);
    HouseFileResponse retrieveHouseFile(Long hostId, Long houseId);
    List<HouseSearchResponse> retrieveHouseSearch(String searchWord, Long userId);
    List<String> retrieveAutoHouseSearch(String searchWord);
    List<HouseElasticSearchResponse> retrieveHouseByElastic(String searchWord);
    List<HouseElasticSearchResponse> retrieveHouseByOption(HouseSearchDto houseSearchDto);
    List<HouseElasticSearchResponse> retrieveHouseByRegion(String region);
    List<HouseCountResponse> retrieveHouseCountByRegion();
    HouseTotalResponse retrieveHouseTotal(Long houseId, Long userId);
    void createHouseDocument();
    List<PopularHouseDto> retrievePopularHouses();
    List<PopularHouseDto> retrieveMyHouses();
    List<PopularHouseDto> retrieveShareList();
}
