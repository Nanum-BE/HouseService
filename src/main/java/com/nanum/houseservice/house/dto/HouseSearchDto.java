package com.nanum.houseservice.house.dto;

import com.nanum.config.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseSearchDto {
    private String houseName;
    private String streetAddress;
    private String zipCode;
    private Gender houseGender;
}
