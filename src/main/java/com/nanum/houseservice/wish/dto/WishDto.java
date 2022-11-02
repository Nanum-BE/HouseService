package com.nanum.houseservice.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {
    private Long wishId;
    private Long houseId;
    private String lotAddress;
    private String houseName;
    private String mainHouseImgPath;
    private Long userId;
    private Integer minMonthlyRent;
    private Integer maxMonthlyRent;
    private Long wishCount;
    private Long reviewCount;
    private Double reviewAvg;
}
