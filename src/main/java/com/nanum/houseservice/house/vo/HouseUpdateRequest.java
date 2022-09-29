package com.nanum.houseservice.house.vo;

import com.nanum.config.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HouseUpdateRequest {
    @NotNull(message = "streetAddress cannot be null")
    @Schema(description = "도로명 주소", defaultValue = "부산광역시 부산진구 엄광로 999")
    private String streetAddress;

    @NotNull(message = "lotAddress cannot be null")
    @Schema(description = "지번번 주소", defaultValue = "부산광역시 부산진구 가야동 999-12")
    private String lotAddress;

    @NotNull(message = "zipCode cannot be null")
    @Schema(description = "우편번호", defaultValue = "99999")
    private String zipCode;

    @NotNull(message = "explanation cannot be null")
    @Schema(description = "하우스 소개", defaultValue = "수정 테스트입니다.")
    private String explanation;

    @NotNull(message = "houseName cannot be null")
    @Schema(description = "하우스 이름", defaultValue = "가야OO하우스")
    private String houseName;

    @NotNull(message = "houseType cannot be null")
    @Schema(description = "건물 형태", defaultValue = "원룸형")
    private String houseType;

    @NotNull(message = "lat cannot be null")
    @Schema(description = "위도", defaultValue = "35.149196162243335")
    private String lat;

    @NotNull(message = "lon cannot be null")
    @Schema(description = "경도", defaultValue = "129.03583544451283")
    private String lon;

    @NotNull(message = "houseGender cannot be null")
    @Schema(description = "하우스 성별", defaultValue = "MALE")
    private Gender houseGender;

    @Schema(description = "검색 키워드", defaultValue = "#수정")
    private String keyWord;

    @Schema(description = "삭제할 하우스 옵션")
    private List<Long> deleteHouseOption;

    @Schema(description = "추가할 하우스 옵션")
    private List<Long> createHouseOption;
}
