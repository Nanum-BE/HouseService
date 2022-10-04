package com.nanum.houseservice.review.dto;

import com.nanum.houseservice.review.domain.Review;
import com.nanum.houseservice.room.domain.Room;
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
    private Long roomId;
    private Long userId;
    private String nickname;
    private int score;
    private String title;
    private String content;

    public Review reviewDtoToEntity(Room room) {
        return Review.builder()
                .room(room)
                .userId(userId)
                .nickname(nickname)
                .score(score)
                .title(title)
                .content(content)
                .build();
    }
}
