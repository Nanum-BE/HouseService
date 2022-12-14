package com.nanum.houseservice.wish.vo;

import com.nanum.config.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishResponse {
    @Schema(description = "좋아요 식별자")
    private Long wishId;

    @Schema(description = "하우스 식별자")
    private Long houseId;

    @Schema(description = "지번 주소")
    private String lotAddress;

    @Schema(description = "하우스 이름")
    private String houseName;

    @Schema(description = "메인 하우스 이미지 경로")
    private String mainHouseImgPath;

    @Schema(description = "하우스 성별")
    private Gender houseGender;

    @Schema(description = "건물 형태")
    private String houseType;

    @Schema(description = "사용자 식별자")
    private Long userId;

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
}
