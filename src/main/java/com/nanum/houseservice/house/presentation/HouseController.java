package com.nanum.houseservice.house.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.exception.NoHouseFileException;
import com.nanum.houseservice.house.application.HouseService;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.dto.HouseUpdateDto;
import com.nanum.houseservice.house.vo.*;
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
@Tag(name = "하우스", description = "하우스 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
})
public class HouseController {

    private final HouseService houseService;

    @Operation(summary = "하우스 등록 API", description = "호스트가 하우스를 등록하는 요청")
    @PostMapping("/houses/{hostId}")
    public ResponseEntity<Object> createHouse(@PathVariable("hostId") Long hostId,
                                              @Valid @RequestPart HouseRequest houseRequest,
                                              @RequestPart(value = "houseMainImg", required = false) MultipartFile houseMainImg,
                                              @RequestPart(value = "floorPlanImg", required = false) MultipartFile floorPlanImg,
                                              @RequestPart(value = "houseFile", required = false) MultipartFile houseFile,
                                              @RequestPart(value = "houseImgs", required = false) List<MultipartFile> houseImgs) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseDto houseDto = mapper.map(houseRequest, HouseDto.class);
        houseDto.setHostId(hostId);

        if(houseFile == null || houseFile.isEmpty()) {
            throw new NoHouseFileException(String.format("HouseFile Cannot Be Empty"));
        }

        houseService.createHouse(houseDto, houseMainImg, floorPlanImg, houseFile, houseImgs);
        String result = "하우스 등록 신청이 완료되었습니다.";
        BaseResponse<String> response = new BaseResponse<>(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "본인 하우스 목록 조회 API", description = "호스트가 본인 하우스 목록을 조회하는 요청")
    @GetMapping("/houses/{hostId}")
    public ResponseEntity<Object> retrieveHostAllHouses(@PathVariable("hostId") Long hostId) {

        List<HostHouseResponse> response = houseService.retrieveHostAllHouses(hostId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "본인 하우스 상세 조회 API", description = "호스트가 본인 하우스 상세 정보를 조회하는 요청")
    @GetMapping("/houses/{hostId}/{houseId}")
    public ResponseEntity<Object> retrieveHostHouse(@PathVariable("hostId") Long hostId,
                                                    @PathVariable("houseId") Long houseId) {

        HouseResponse response = houseService.retrieveHostHouse(hostId, houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 수정 API", description = "호스트가 본인 하우스 정보 및 하우스 옵션을 수정하는 요청")
    @PutMapping("/houses/{hostId}/{houseId}")
    public ResponseEntity<Object> updateHouse(@PathVariable("hostId") Long hostId,
                                              @PathVariable("houseId") Long houseId,
                                              @Valid @RequestPart HouseUpdateRequest houseUpdateRequest,
                                              @RequestPart(value = "houseMainImg", required = false) MultipartFile houseMainImg,
                                              @RequestPart(value = "floorPlanImg", required = false) MultipartFile floorPlanImg) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseUpdateDto houseUpdateDto = mapper.map(houseUpdateRequest, HouseUpdateDto.class);
        houseUpdateDto.setHostId(hostId);

        houseService.updateHouse(houseId, houseUpdateDto, houseMainImg, floorPlanImg);
        String result = "하우스 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "하우스 이미지 수정 API", description = "호스트가 본인 하우스 상세 이미지를 수정하는 요청")
    @PutMapping("/houses/{hostId}/{houseId}/image")
    public ResponseEntity<Object> updateHouseImg(@PathVariable("hostId") Long hostId,
                                              @PathVariable("houseId") Long houseId,
                                              @RequestPart(required = false) List<Long> deleteHouseImgs,
                                              @RequestPart(required = false) List<MultipartFile> houseImgs) {

        houseService.updateHouseImg(hostId, houseId, deleteHouseImgs, houseImgs);
        String result = "하우스 이미지 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "하우스 서류 수정 API", description = "호스트가 본인 하우스 서류를 수정하는 요청")
    @PutMapping("/houses/{hostId}/{houseId}/file")
    public ResponseEntity<Object> updateHouseFile(@PathVariable("hostId") Long hostId,
                                                 @PathVariable("houseId") Long houseId,
                                                 @RequestPart(required = false) MultipartFile houseFile) {

        if(houseFile == null || houseFile.isEmpty()) {
            throw new NoHouseFileException(String.format("HouseFile Cannot Be Empty"));
        }

        houseService.updateHouseFile(hostId, houseId, houseFile);
        String result = "하우스 이미지 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "하우스 서류 조회 API", description = "하우스 서류를 조회하는 요청")
    @GetMapping("/houses/{hostId}/{houseId}/file")
    public ResponseEntity<Object> retrieveHouseFile(@PathVariable Long hostId,
                                                    @PathVariable Long houseId) {

        HouseFileResponse response = houseService.retrieveHouseFile(hostId, houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

}
