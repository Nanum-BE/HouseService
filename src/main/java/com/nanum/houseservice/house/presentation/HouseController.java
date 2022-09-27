package com.nanum.houseservice.house.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.houseservice.house.application.HouseService;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.vo.HouseRequest;
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
@Tag(name = "하우스", description = "하우스 관련 api")
public class HouseController {

    private final HouseService houseService;
    @Operation(summary = "하우스 등록 API", description = "호스트가 하우스를 등록하는 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PostMapping("/houses/{hostId}")
    public ResponseEntity<Object> createHouse(@PathVariable("hostId") Long hostId,
                                              @Valid @RequestPart HouseRequest houseRequest,
                                              @RequestPart("houseMainImg") MultipartFile houseMainImg,
                                              @RequestPart("floorPlanImg") MultipartFile floorPlanImg,
                                              @RequestPart("houseFile") MultipartFile houseFile,
                                              @RequestPart("houseImgs") List<MultipartFile> houseImgs) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseDto houseDto = mapper.map(houseRequest, HouseDto.class);
        houseDto.setHostId(hostId);

        houseService.createHouse(houseDto, houseMainImg, floorPlanImg, houseFile, houseImgs);
        String result = "하우스 등록 신청이 완료되었습니다.";
        BaseResponse<String> response = new BaseResponse<>(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
