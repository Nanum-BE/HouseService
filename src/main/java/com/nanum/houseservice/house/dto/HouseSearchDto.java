package com.nanum.houseservice.house.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseSearchDto {
    private final String searchWord;
    private final String area;
    private final String genderType;
    private final String houseType;
    private final Double centerX;
    private final Double centerY;
    private final Double southWestX;
    private final Double southWestY;
    private Double distance;

    @Builder
    public HouseSearchDto(String searchWord, String area, String genderType, String houseType, Double centerX, Double centerY, Double southWestX, Double southWestY) {
        this.searchWord = searchWord;
        this.area = area;
        this.genderType = genderType;
        this.houseType = houseType;
        this.centerX = centerX;
        this.centerY = centerY;
        this.southWestX = southWestX;
        this.southWestY = southWestY;
    }
}
