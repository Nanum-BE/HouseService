package com.nanum.houseservice.review.vo;

import com.nanum.houseservice.review.domain.Review;
import com.nanum.houseservice.review.domain.ReviewImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewShortResponse {
    @Schema(description = "리뷰 식별자")
    private Long id;
    @Schema(description = "작성자 식별자")
    private Long userId;
    @Schema(description = "작성자 닉네임")
    private String nickname;

    @Schema(description = "리뷰 점수")
    private Integer score;
    @Schema(description = "리뷰 제목")
    private String title;

    @Schema(description = "리뷰 생성 일자")
    private LocalDateTime createAt;
    @Schema(description = "리뷰 수정 일자")
    private LocalDateTime updateAt;
    @Schema(description = "리뷰 이미지")
    private ReviewImgResponse reviewImg;

    public ReviewShortResponse entityToReviewShortResponse(Review review, ReviewImgResponse reviewImgResponse) {
        return ReviewShortResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .nickname(review.getNickname())
                .score(review.getScore())
                .title(review.getTitle())
                .createAt(review.getCreateAt())
                .updateAt(review.getUpdateAt())
                .reviewImg(reviewImgResponse)
                .build();
    }
}
