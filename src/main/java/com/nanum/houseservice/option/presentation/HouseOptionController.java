package com.nanum.houseservice.option.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.exception.ExceptionResponse;
import com.nanum.houseservice.house.vo.HostHouseResponse;
import com.nanum.houseservice.option.application.HouseOptionService;
import com.nanum.houseservice.option.vo.HouseOptionCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "하우스 옵션", description = "하우스 옵션 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
})
public class HouseOptionController {

    private final HouseOptionService houseOptionService;

    @Operation(summary = "하우스 옵션 전체 조회 API", description = "하우스 옵션 목록을 조회하는 요청")
    @GetMapping("/houses/options")
    public ResponseEntity<Object> retrieveOptions() {

        List<HouseOptionCheckResponse> response = houseOptionService.retrieveOptions();

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }
}
