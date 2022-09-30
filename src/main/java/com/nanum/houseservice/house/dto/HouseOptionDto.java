package com.nanum.houseservice.house.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseOptionDto {
    private Long houseOptionConnId;
    private Long houseOptionOptionName;
    private Long houseOptionIconPath;
}
