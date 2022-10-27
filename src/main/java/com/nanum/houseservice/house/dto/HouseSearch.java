package com.nanum.houseservice.house.dto;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.vo.HouseSearchResponse;
import com.nanum.houseservice.house.vo.HouseTotalResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class HouseSearch {
    private House house;
    private Integer maxMonthlyRent;
    private Integer minMonthlyRent;
    private Long wishCount;
    private Long reviewCount;
    private Double reviewAvg;

    public HouseSearchResponse toSearchResponse(Long wishId) {
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
                .wishCount(wishCount)
                .reviewCount(reviewCount)
                .reviewAvg(reviewAvg != null ? Double.parseDouble(String.format("%.1f", reviewAvg)) : 0)
                .wishId(wishId)
                .build();
    }

    public HouseTotalResponse from(Long wishId) {
        return HouseTotalResponse.builder()
                .id(house.getId())
                .minMonthlyRent(minMonthlyRent != null ? minMonthlyRent : 0)
                .maxMonthlyRent(maxMonthlyRent != null ? maxMonthlyRent : 0)
                .wishCount(wishCount)
                .reviewCount(reviewCount)
                .reviewAvg(reviewAvg != null ? Double.parseDouble(String.format("%.1f", reviewAvg)) : 0)
                .wishId(wishId)
                .build();
    }
}
