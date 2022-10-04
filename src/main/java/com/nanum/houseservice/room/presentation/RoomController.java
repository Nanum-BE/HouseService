package com.nanum.houseservice.room.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.houseservice.house.dto.HouseUpdateDto;
import com.nanum.houseservice.room.application.RoomService;
import com.nanum.houseservice.room.dto.RoomDto;
import com.nanum.houseservice.room.dto.RoomUpdateDto;
import com.nanum.houseservice.room.vo.HostRoomResponse;
import com.nanum.houseservice.room.vo.RoomRequest;
import com.nanum.houseservice.room.vo.RoomResponse;
import com.nanum.houseservice.room.vo.RoomUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "방", description = "방 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
})
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "방 등록 API", description = "호스트가 하우스의 방을 등록하는 요청")
    @PostMapping("/houses/{houseId}/rooms")
    public ResponseEntity<Object> createRoom(@Valid @RequestPart RoomRequest roomRequest,
                                             @PathVariable Long houseId,
                                             @RequestPart(value = "roomMainImg", required = false) MultipartFile roomMainImg,
                                             @RequestPart(value = "roomImgs", required = false) List<MultipartFile> roomImgs) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RoomDto roomDto = mapper.map(roomRequest, RoomDto.class);
        roomDto.setHouseId(houseId);

        roomService.createRoom(roomDto, roomMainImg, roomImgs);
        String result = "방 등록이 완료되었습니다.";
        BaseResponse<String> response = new BaseResponse<>(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "방 목록 조회 API", description = "호스트가 하우스의 방 목록을 조회하는 요청")
    @GetMapping("/houses/{houseId}/rooms")
    public ResponseEntity<Object> retrieveHostAllRooms(@PathVariable Long houseId) {
        List<RoomResponse> response = roomService.retrieveHostAllRooms(houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "방 상세 조회 API", description = "호스트가 하우스의 특정 방 정보를 조회하는 요청")
    @GetMapping("/houses/{houseId}/rooms/{roomId}")
    public ResponseEntity<Object> retrieveHostRoom(@PathVariable Long houseId, @PathVariable Long roomId) {
        HostRoomResponse response = roomService.retrieveHostRoom(houseId, roomId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "방 정보 수정 API", description = "호스트가 하우스의 특정 방 정보를 수정하는 요청")
    @PutMapping("/houses/{houseId}/rooms/{roomId}")
    public ResponseEntity<Object> updateRoom(@PathVariable Long houseId,
                                             @PathVariable Long roomId,
                                             @Valid @RequestPart RoomUpdateRequest roomRequest,
                                             @RequestPart(value = "roomMainImg", required = false) MultipartFile roomMainImg) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RoomUpdateDto roomUpdateDto = mapper.map(roomRequest, RoomUpdateDto.class);

        roomService.updateRoom(houseId, roomId, roomUpdateDto, roomMainImg);
        String result = "방 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "방 이미지 수정 API", description = "호스트가 하우스의 특정 방 이미지를 수정하는 요청")
    @PutMapping("/houses/{houseId}/rooms/{roomId}/image")
    public ResponseEntity<Object> updateRoomImg(@PathVariable Long houseId,
                                                @PathVariable Long roomId,
                                                @RequestPart(required = false) List<Long> deleteRoomImgs,
                                                @RequestPart(required = false) List<MultipartFile> roomImgs) {
        roomService.updateRoomImg(houseId, roomId, deleteRoomImgs, roomImgs);
        String result = "방 이미지 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    //TODO #6: 방 삭제 API


}
