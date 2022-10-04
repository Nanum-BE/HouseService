package com.nanum.houseservice.review.dto;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long houseId;
    private Long userId;
    private int score;
    private String title;
    private String content;

    public Review reviewDtoToEntity(House house) {
        return Review.builder()
                .house(house)
                .userId(userId)
                .score(score)
                .title(title)
                .content(content)
                .build();
    }
}
