package com.nanum.houseservice.house.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseOriginResponse {

    @Schema(description = "houseRequest 객체")
    HouseRequest houseRequest;

    @Schema(description = "하우스 메인 이미지 경로")
    String houseMainImg;

    @Schema(description = "하우스 도면 이미지 경로")
    String floorPlanImg;

    @Schema(description = "하우스 서류 경로")
    String houseFile;

    @Schema(description = "하우스 상세 이미지 객체")
    List<HouseImgResponse> houseImgs;
}
