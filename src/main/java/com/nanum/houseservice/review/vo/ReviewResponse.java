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
public class ReviewResponse {
    @Schema(description = "리뷰 식별자")
    private Long id;
    @Schema(description = "작성자 식별자")
    private Long userId;
    @Schema(description = "작성자 닉네임")
    private String nickname;

    @Schema(description = "방 식별자")
    private Long roomId;
    @Schema(description = "방 이름")
    private String roomName;

    @Schema(description = "리뷰 점수")
    private Integer score;
    @Schema(description = "리뷰 제목")
    private String title;
    @Schema(description = "리뷰 내용")
    private String content;
    @Schema(description = "리뷰 생성 일자")
    private LocalDateTime createAt;
    @Schema(description = "리뷰 수정 일자")
    private LocalDateTime updateAt;
    @Schema(description = "리뷰 이미지")
    private List<ReviewImgResponse> reviewImgs;

    public ReviewResponse entityToReviewResponse(Review review, List<ReviewImgResponse> reviewImgs) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .nickname(review.getNickname())
                .roomId(review.getRoom().getId())
                .roomName(review.getRoom().getName())
                .score(review.getScore())
                .title(review.getTitle())
                .content(review.getContent())
                .createAt(review.getCreateAt())
                .updateAt(review.getUpdateAt())
                .reviewImgs(reviewImgs)
                .build();
    }
}
