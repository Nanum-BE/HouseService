package com.nanum.houseservice.house.application;

import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.vo.HostHouseResponse;
import com.nanum.houseservice.house.vo.HouseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HouseService {
    void createHouse(HouseDto houseDto, MultipartFile houseMainImg,
                     MultipartFile floorPlanImg, MultipartFile houseFile,
                     List<MultipartFile> houseImgs);

    List<HostHouseResponse> retrieveHostAllHouses(Long hostId);
    HouseResponse retrieveHostHouse(Long hostId, Long houseId);

}
