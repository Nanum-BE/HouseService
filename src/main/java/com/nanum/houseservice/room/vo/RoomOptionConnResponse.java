package com.nanum.houseservice.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomOptionConnResponse {
    @Schema(description = "방 옵션 연결 식별자")
    private Long id;

    @Schema(description = "연결된 방 옵션 이름")
    private String optionName;

    @Schema(description = "연결된 방 옵션 아이콘 경로")
    private String iconPath;
}
