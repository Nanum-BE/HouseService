package com.nanum.houseservice.room.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.exception.NoHouseFileException;
import com.nanum.houseservice.room.application.RoomService;
import com.nanum.houseservice.room.dto.RoomDto;
import com.nanum.houseservice.room.vo.RoomRequest;
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
@RequestMapping("/api")
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
    @PostMapping("/v1/rooms")
    public ResponseEntity<Object> createRoom(@Valid @RequestPart RoomRequest roomRequest,
                                             @RequestPart(value = "roomMainImg", required = false) MultipartFile roomMainImg,
                                             @RequestPart(value = "roomImgs", required = false) List<MultipartFile> roomImgs) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RoomDto roomDto = mapper.map(roomRequest, RoomDto.class);

        roomService.createRoom(roomDto, roomMainImg, roomImgs);
        String result = "방 등록이 완료되었습니다.";
        BaseResponse<String> response = new BaseResponse<>(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //TODO #2: 방 목록 조회 API


    //TODO #3: 방 상세 조회 API


    //TODO #4: 방 수정 API


    //TODO #5: 방 삭제 API


}
