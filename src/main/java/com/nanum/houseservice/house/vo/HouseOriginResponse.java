package com.nanum.houseservice.house.vo;

import com.nanum.config.Gender;
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
    @Schema(description = "호스트 식별자", defaultValue = "1")
    private Long hostId;

    @Schema(description = "도로명 주소", defaultValue = "부산광역시 부산진구 엄광로 999")
    private String streetAddress;

    @Schema(description = "지번번 주소", defaultValue = "부산광역시 부산진구 가야동 999-12")
    private String lotAddress;

    @Schema(description = "상세 주소", defaultValue = "OO빌라 3~4층")
    private String detailAddress;

    @Schema(description = "우편번호", defaultValue = "99999")
    private String zipCode;

    @Schema(description = "하우스 소개", defaultValue = "신축 건물입니다~")
    private String explanation;

    @Schema(description = "하우스 이름", defaultValue = "가야OO하우스")
    private String houseName;

    @Schema(description = "건물 형태", defaultValue = "원룸형")
    private String houseType;

    @Schema(description = "위도", defaultValue = "35.149196162243335")
    private String lat;

    @Schema(description = "경도", defaultValue = "129.03583544451283")
    private String lon;

    @Schema(description = "하우스 성별", defaultValue = "MALE")
    private Gender houseGender;

    @Schema(description = "검색 키워드", defaultValue = "가야역")
    private List<String> keyWord;

    @Schema(description = "하우스 옵션")
    private List<Long> houseOption;

    @Schema(description = "메인 이미지 경로")
    private String mainHouseImgPath;

    @Schema(description = "도면 이미지 경로")
    private String floorPlanPath;

    @Schema(description = "하우스 이미지 반환 객체")
    private List<HouseImgResponse> houseImgs;
}
