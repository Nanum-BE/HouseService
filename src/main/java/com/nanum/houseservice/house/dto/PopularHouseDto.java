package com.nanum.houseservice.house.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.config.Gender;
import com.nanum.houseservice.house.domain.House;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PopularHouseDto {
    private Long id;

    private String mainHouseImgPath;

    private String houseName;

    private Gender houseGender;

    private String streetAddress;

    public static PopularHouseDto of(House house) {
        PopularHouseDto popularHouseDto = new PopularHouseDto();
        popularHouseDto.id = house.getId();
        popularHouseDto.mainHouseImgPath = house.getMainHouseImgPath();
        popularHouseDto.houseName = house.getHouseName();
        popularHouseDto.houseGender = house.getHouseGender();
        popularHouseDto.streetAddress = house.getStreetAddress();
        return popularHouseDto;
    }
}
