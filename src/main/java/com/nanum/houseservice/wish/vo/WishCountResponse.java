package com.nanum.houseservice.wish.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishCountResponse {

    @Schema(description = "좋아요 개수")
    private Long wishCount;
}
