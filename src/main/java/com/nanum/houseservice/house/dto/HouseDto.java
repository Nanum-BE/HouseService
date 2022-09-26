package com.nanum.houseservice.house.dto;

import com.nanum.config.Gender;
import com.nanum.config.HouseStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseDto {
    private Long hostId;
    private String streetAddress;
    private String lotAddress;
    private String zipCode;
    private String explanation;
    private String houseName;
    private String houseType;
    private String lat;
    private String lon;
    private Gender houseGender;
    private String keyWord;
    private HouseStatus status;
}
