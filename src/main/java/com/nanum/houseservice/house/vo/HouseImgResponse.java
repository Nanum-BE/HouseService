package com.nanum.houseservice.house.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseImgResponse {

    @Schema(description = "하우스 이미지 식별자")
    private Long houseImgId;

    @Schema(description = "하우스 이미지 경로")
    private String imgPath;
}
