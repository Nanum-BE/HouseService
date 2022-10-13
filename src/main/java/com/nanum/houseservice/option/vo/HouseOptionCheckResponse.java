package com.nanum.houseservice.option.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HouseOptionCheckResponse {
    @Schema(description = "하우스 옵션 식별자")
    private Long houseOptionId;

    @Schema(description = "하우스 옵션 이름")
    private String optionName;

    @Schema(description = "하우스 옵션 선택 여부")
    private Boolean isChecked;

    @Schema(description = "하우스 옵션 경로")
    private String iconPath;
}
