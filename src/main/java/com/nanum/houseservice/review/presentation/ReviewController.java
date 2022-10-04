package com.nanum.houseservice.review.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.houseservice.review.application.ReviewService;
import com.nanum.houseservice.review.dto.ReviewDto;
import com.nanum.houseservice.review.vo.ReviewRequest;
import com.nanum.houseservice.review.vo.ReviewResponse;
import com.nanum.houseservice.review.vo.ReviewShortResponse;
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
@Tag(name = "리뷰", description = "리뷰 관련 API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "204", description = "deleted successfully"),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
})
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록 API", description = "사용자가 리뷰를 등록하는 요청")
    @PostMapping("/houses/{houseId}/reviews")
    public ResponseEntity<Object> createReview(@PathVariable Long houseId,
                                               @Valid @RequestPart ReviewRequest reviewRequest,
                                               @RequestPart(required = false) List<MultipartFile> reviewImgs) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ReviewDto reviewDto = mapper.map(reviewRequest, ReviewDto.class);
        reviewDto.setHouseId(houseId);

        reviewService.createReview(reviewDto, reviewImgs);
        String result = "리뷰 등록이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(result));
    }

    @Operation(summary = "리뷰 목록 조회 API", description = "사용자가 하우스의 리뷰 목록을 조회하는 요청")
    @GetMapping("/houses/{houseId}/reviews")
    public ResponseEntity<Object> retrieveHouseReviews(@PathVariable Long houseId) {

        List<ReviewShortResponse> reviewResponse = reviewService.retrieveHouseReviews(houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(reviewResponse));
    }

    @Operation(summary = "리뷰 조회 API", description = "사용자가 특정 리뷰를 상세 조회하는 요청")
    @GetMapping("/houses/{houseId}/reviews/{reviewId}")
    public ResponseEntity<Object> retrieveReview(@PathVariable Long houseId, @PathVariable Long reviewId) {

        ReviewResponse reviewResponse = reviewService.retrieveReview(houseId, reviewId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(reviewResponse));
    }

    @Operation(summary = "리뷰 수정 API", description = "사용자가 하우스 리뷰를 수정하는 요청")
    @PutMapping("/houses/{houseId}/reviews/{reviewId}")
    public ResponseEntity<Object> updateReview(@PathVariable Long houseId,
                                               @PathVariable Long reviewId,
                                               @Valid @RequestBody ReviewRequest reviewRequest) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ReviewDto reviewDto = mapper.map(reviewRequest, ReviewDto.class);
        reviewDto.setHouseId(houseId);

        reviewService.updateReview(reviewDto, reviewId);
        String result = "하우스 리뷰 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "리뷰 이미지 수정 API", description = "사용자가 하우스 리뷰 이미지를 수정하는 요청")
    @PutMapping("/houses/{houseId}/reviews/{reviewId}/image")
    public ResponseEntity<Object> updateReviewImg(@PathVariable Long houseId,
                                                  @PathVariable Long reviewId,
                                                  @RequestPart(required = false) List<Long> deleteReviewImgs,
                                                  @RequestPart(required = false) List<MultipartFile> reviewImgs) {

        reviewService.updateReviewImg(houseId, reviewId, deleteReviewImgs, reviewImgs);
        String result = "하우스 리뷰 이미지 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }
}
