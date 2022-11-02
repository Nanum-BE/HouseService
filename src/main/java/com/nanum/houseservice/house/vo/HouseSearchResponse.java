package com.nanum.houseservice.house.vo;

import com.nanum.config.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HouseSearchResponse {

    @Schema(description = "하우스 식별자")
    private Long id;

    @Schema(description = "하우스 이름")
    private String houseName;

    @Schema(description = "하우스 메인 사진 URL")
    private String mainHouseImgPath;

    @Schema(description = "하우스 성별 타입")
    private Gender houseGender;

    @Schema(description = "하우스 도로명 주소")
    private String streetAddress;

    @Schema(description = "위도")
    private String lat;

    @Schema(description = "경도")
    private String lon;

    @Schema(description = "최저 가격")
    private Integer minMonthlyRent;

    @Schema(description = "최대 가격")
    private Integer maxMonthlyRent;

    @Schema(description = "좋아요 개수")
    private Long wishCount;

    @Schema(description = "리뷰 개수")
    private Long reviewCount;

    @Schema(description = "리뷰 평점")
    private String reviewAvg;

    @Schema(description = "좋아요 식별자")
    private Long wishId;
}
