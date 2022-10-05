package com.nanum.houseservice.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImgResponse {
    @Schema(description = "리뷰 이미지 식별자")
    private Long id;

    @Schema(description = "리뷰 이미지 경로")
    private String imgPath;
}
