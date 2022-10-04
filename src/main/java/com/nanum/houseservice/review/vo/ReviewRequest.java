package com.nanum.houseservice.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ReviewRequest {
    @NotNull(message = "userId cannot be null")
    @Schema(description = "유저 식별자", defaultValue = "1")
    private Long userId;

    @NotNull(message = "roomId cannot be null")
    @Schema(description = "방 식별자", defaultValue = "1")
    private Long roomId;

    @NotNull(message = "score cannot be null")
    @Max(value = 5)
    @Min(value = 1)
    @Schema(description = "리뷰 점수(1~5점)", defaultValue = "4")
    private int score;

    @NotNull(message = "title cannot be null")
    @Schema(description = "리뷰 제목", defaultValue = "잘 지내다 가요~")
    private String title;

    @NotNull(message = "content cannot be null")
    @Schema(description = "리뷰 내용", defaultValue = "서울에 혼자 올라와서 지내기 외로웠는데" +
                                                            "여기 사람들과 좋은 추억 남겼어요!")
    private String content;
}
