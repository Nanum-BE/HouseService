package com.nanum.houseservice.house.vo;

import com.nanum.config.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HouseRequest {
    @NotNull
    @Schema(description = "도로명 주소", defaultValue = "부산광역시 부산진구 엄광로 999")
    private String streetAddress;

    @NotNull
    @Schema(description = "지번번 주소", defaultValue = "부산광역시 부산진구 가야동 999-12")
    private String lotAddress;

    @NotNull
    @Schema(description = "우편번호", defaultValue = "99999")
    private String zipCode;

    @NotNull
    @Schema(description = "하우스 소개", defaultValue = "신축 건물입니다~")
    private String explanation;

    @NotNull
    @Schema(description = "하우스 이름", defaultValue = "가야OO하우스")
    private String houseName;

    @NotNull
    @Schema(description = "건물 형태", defaultValue = "원룸형")
    private String houseType;

    @NotNull
    @Schema(description = "위도", defaultValue = "35.149196162243335")
    private String lat;

    @NotNull
    @Schema(description = "경도", defaultValue = "129.03583544451283")
    private String lon;

    @NotNull
    @Schema(description = "하우스 성별", defaultValue = "MALE")
    private Gender houseGender;

    @Schema(description = "검색 키워드", defaultValue = "#가야역#동의대역#동의대학교")
    private String keyWord;
}
