package com.nanum.houseservice.room.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostRoomResponse {
    @Schema(description = "방 객체")
    private RoomResponse room;

    @Schema(description = "방 이미지 리스트")
    private List<RoomImgResponse> roomImgs;

    @Schema(description = "방 옵션 리스트")
    private List<RoomOptionConnResponse> roomOptionConn;
}
