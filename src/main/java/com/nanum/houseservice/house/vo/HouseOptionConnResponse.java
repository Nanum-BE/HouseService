package com.nanum.houseservice.house.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseOptionConnResponse {

    @Schema(description = "하우스 옵션 연결 식별자")
    private Long houseOptionConnId;

    @Schema(description = "연결된 하우스 옵션 이름")
    private String optionName;

    @Schema(description = "연결된 하우스 옵션 아이콘 경로")
    private String iconPath;
}
