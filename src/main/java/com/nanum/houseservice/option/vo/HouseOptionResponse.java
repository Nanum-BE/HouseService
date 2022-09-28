package com.nanum.houseservice.option.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HouseOptionResponse {
    @Schema(description = "하우스 옵션 식별자")
    private Long houseOptionId;

    @Schema(description = "하우스 옵션 이름")
    private Long optionName;

    @Schema(description = "하우스 옵션 경로")
    private Long iconPath;
}
