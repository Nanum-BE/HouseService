package com.nanum.houseservice.wish.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.exception.CustomizedResponseEntityExceptionHandler;
import com.nanum.exception.ExceptionResponse;
import com.nanum.exception.OverlapException;
import com.nanum.houseservice.wish.application.WishService;
import com.nanum.houseservice.wish.dto.WishDto;
import com.nanum.houseservice.wish.vo.WishCountResponse;
import com.nanum.houseservice.wish.vo.WishIdResponse;
import com.nanum.houseservice.wish.vo.WishRequest;
import com.nanum.houseservice.wish.vo.WishResponse;
import com.nanum.util.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "좋아요", description = "좋아요 관련 API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "204", description = "deleted successfully"),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "409", description = "conflicted data", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
})
public class WishController {
    private final WishService wishService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "좋아요 API", description = "사용자가 하우스 좋아요를 추가하는 요청")
    @PostMapping("/users/{userId}/wishes")
    public ResponseEntity<Object> createWish(@PathVariable Long userId, @RequestBody WishRequest wishRequest) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        WishDto wishDto = mapper.map(wishRequest, WishDto.class);
        wishDto.setUserId(userId);

        WishIdResponse wishIdResponse = wishService.createWish(wishDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(wishIdResponse));
    }

    @Operation(summary = "좋아요 취소 API", description = "사용자가 하우스 좋아요를 취소하는 요청")
    @DeleteMapping("/users/{userId}/wishes/{wishId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteWish(@PathVariable Long userId, @PathVariable Long wishId) {
        wishService.deleteWish(userId, wishId);
    }

    @Operation(summary = "좋아요 조회 API", description = "사용자가 좋아요 목록을 조회하는 요청")
    @GetMapping("/users/{userId}/wishes")
    public ResponseEntity<Object> retrieveWish(@PathVariable Long userId,
                                               Pageable pageable) {
        Page<WishResponse> wishResponses = wishService.retrieveWish(userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(wishResponses));
    }

    @Operation(summary = "내 좋아요 개수 조회 API", description = "특정 사용자의 좋아요 개수를 조회하는 요청")
    @GetMapping("/users/wishes")
    public ResponseEntity<Object> retrieveWishCount() {
        String token = jwtProvider.customResolveToken();
        Long userId = token != null ? Long.valueOf(jwtProvider.getUserPk(token)) : null;
        WishCountResponse wishResponses;

        if(userId != null) {
            wishResponses = wishService.retrieveWishCount(userId);
        } else {
            wishResponses = new WishCountResponse(0L);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(wishResponses));
    }
}
