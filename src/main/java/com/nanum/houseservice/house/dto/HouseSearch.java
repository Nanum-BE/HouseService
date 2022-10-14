package com.nanum.houseservice.house.dto;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.vo.HouseSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HouseSearch {
    private House house;
    private Integer maxMonthlyRent;
    private Integer minMonthlyRent;

    public HouseSearchResponse toSearchResponse() {
        return HouseSearchResponse.builder()
                .id(house.getId())
                .houseName(house.getHouseName())
                .mainHouseImgPath(house.getMainHouseImgPath())
                .houseGender(house.getHouseGender())
                .streetAddress(house.getStreetAddress())
                .lat(house.getLat())
                .lon(house.getLon())
                .minMonthlyRent(minMonthlyRent)
                .maxMonthlyRent(maxMonthlyRent)
                .wishCount(null)
                .reviewCount(null)
                .reviewAvg(null)
                .build();
    }
}
