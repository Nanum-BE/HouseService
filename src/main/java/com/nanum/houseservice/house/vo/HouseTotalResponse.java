package com.nanum.houseservice.house.vo;

import com.nanum.houseservice.house.dto.HouseSearch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseTotalResponse {

    @Schema(description = "하우스 식별자")
    private Long id;

    @Schema(description = "최저 가격")
    private Integer minMonthlyRent;

    @Schema(description = "최대 가격")
    private Integer maxMonthlyRent;

    @Schema(description = "좋아요 개수")
    private Long wishCount;

    @Schema(description = "리뷰 개수")
    private Long reviewCount;

    @Schema(description = "리뷰 평점")
    private Double reviewAvg;

    @Schema(description = "좋아요 식별자")
    private Long wishId;


}
