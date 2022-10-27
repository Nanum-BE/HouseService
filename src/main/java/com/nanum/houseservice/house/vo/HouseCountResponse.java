package com.nanum.houseservice.house.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseCountResponse {
    @Schema(description = "지역명")
    private String region;

    @Schema(description = "하우스 개수")
    private Long houseCount;
}
