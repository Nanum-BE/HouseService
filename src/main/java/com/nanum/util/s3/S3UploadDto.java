package com.nanum.util.s3;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.domain.HouseFile;
import com.nanum.houseservice.house.domain.HouseImg;
import com.nanum.houseservice.review.domain.Review;
import com.nanum.houseservice.review.domain.ReviewImg;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.houseservice.room.domain.RoomImg;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class S3UploadDto {

    private String originName;
    private String saveName;
    private String imgUrl;

    public HouseFile houseFileToEntity(House house) {
        return HouseFile.builder()
                .house(house)
                .originName(originName)
                .saveName(saveName)
                .filePath(imgUrl)
                .build();
    }

    public HouseImg houseImgToEntity(House house) {
        return HouseImg.builder()
                .house(house)
                .originName(originName)
                .saveName(saveName)
                .imgPath(imgUrl)
                .build();
    }

    public RoomImg roomImgToEntity(Room room) {
        return RoomImg.builder()
                .room(room)
                .originName(originName)
                .saveName(saveName)
                .imgPath(imgUrl)
                .build();
    }

    public ReviewImg reviewImgToEntity(Review review) {
        return ReviewImg.builder()
                .review(review)
                .originName(originName)
                .saveName(saveName)
                .imgPath(imgUrl)
                .build();
    }

}
