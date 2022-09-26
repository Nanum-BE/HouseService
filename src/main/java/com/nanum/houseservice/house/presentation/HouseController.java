package com.nanum.houseservice.house.presentation;

import com.nanum.error.BaseResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    @PostMapping("/houses")
    public ResponseEntity<Object> createHouse(@Valid @RequestBody HouseRequest houseRequest) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseDto houseDto = mapper.map(houseRequest, HouseDto.class);
        houseService.createHouse(houseDto);
        String result = "하우스 등록 신청이 완료되었습니다.";
        BaseResponse<String> response = new BaseResponse<>(HttpStatus.CREATED.value(), result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
