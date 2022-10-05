package com.nanum.houseservice.house.vo;

import com.nanum.config.Gender;
import com.nanum.config.HouseStatus;
import com.nanum.houseservice.house.domain.HouseDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseResponse {

    @Schema(description = "하우스 식별자")
    private Long id;

    @Schema(description = "도로명 주소")
    private String streetAddress;

    @Schema(description = "지번 주소")
    private String lotAddress;

    @Schema(description = "우편번호")
    private String zipCode;

    @Schema(description = "하우스 소개")
    private String explanation;

    @Schema(description = "하우스 이름")
    private String houseName;

    @Schema(description = "건물 형태")
    private String houseType;

    @Schema(description = "위도")
    private String lat;

    @Schema(description = "경도")
    private String lon;

    @Schema(description = "하우스 성별")
    private Gender houseGender;

    @Schema(description = "메인 이미지 경로")
    private String mainHouseImgPath;

    @Schema(description = "도면 이미지 경로")
    private String floorPlanPath;

    @Schema(description = "검색 키워드")
    private String keyWord;

    @Schema(description = "하우스 상태")
    private HouseStatus status;

    @Schema(description = "하우스 이미지 반환 객체")
    private List<HouseImgResponse> houseImgs;

    @Schema(description = "하우스 옵션 연결 반환 객체")
    private List<HouseOptionConnResponse> houseOptionConn;

    public static HouseResponse from(HouseDocument house) {
        return HouseResponse.builder()
                .id(house.getId())
                .streetAddress(house.getStreetAddress())
                .zipCode(house.getZipCode())
                .explanation(house.getExplanation())
                .houseName(house.getHouseName())
                .houseType(house.getHouseType())
                .lat(house.getLat())
                .lon(house.getLon())
                .houseGender(house.getHouseGender())
                .mainHouseImgPath(house.getMainHouseImgPath())
                .floorPlanPath(house.getFloorPlanPath())
                .status(house.getStatus())
                .build();
    }
}
