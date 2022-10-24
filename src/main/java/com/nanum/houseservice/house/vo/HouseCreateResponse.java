package com.nanum.houseservice.house.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HouseCreateResponse {

    @Schema(description = "하우스 식별자")
    private Long id;

    @Schema(description = "하우스 이름")
    private String houseName;

    @Schema(description = "하우스 메인 사진 경로")
    private String mainHouseImgPath;
}
