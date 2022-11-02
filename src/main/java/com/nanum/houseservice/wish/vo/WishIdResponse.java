package com.nanum.houseservice.wish.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishIdResponse {
    @Schema(description = "좋아요 식별자")
    private Long wishId;
}
