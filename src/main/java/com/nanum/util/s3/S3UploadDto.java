package com.nanum.util.s3;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.domain.HouseFile;
import com.nanum.houseservice.house.domain.HouseImg;
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

    public HouseImg houseImgToEntity(House house, int priority) {
        return HouseImg.builder()
                .house(house)
                .originName(originName)
                .saveName(saveName)
                .imgPath(imgUrl)
                .priority(priority)
                .build();
    }
}
