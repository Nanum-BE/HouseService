package com.nanum.houseservice.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomImgResponse {

    @Schema(description = "방 이미지 식별자")
    private Long id;

    @Schema(description = "방 이미지 경로")
    private String imgPath;
}