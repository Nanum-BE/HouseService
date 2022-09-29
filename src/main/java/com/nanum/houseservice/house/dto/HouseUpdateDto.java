package com.nanum.houseservice.house.dto;

import com.nanum.config.Gender;
import com.nanum.config.HouseStatus;
import com.nanum.houseservice.house.domain.House;
import com.nanum.util.s3.S3UploadDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseUpdateDto {
    private Long hostId;
    private String streetAddress;
    private String lotAddress;
    private String zipCode;
    private String explanation;
    private String houseName;
    private String houseType;
    private String lat;
    private String lon;
    private Gender houseGender;
    private String keyWord;
    private HouseStatus status;
    private List<Long> deleteHouseOption;
    private List<Long> createHouseOption;

    public House houseDtoToEntity(S3UploadDto mainImgUrl, S3UploadDto floorPlanImgUrl, Long houseId) {
        return House.builder()
                .id(houseId)
                .hostId(hostId)
                .streetAddress(streetAddress)
                .lotAddress(lotAddress)
                .zipCode(zipCode)
                .explanation(explanation)
                .houseName(houseName)
                .houseType(houseType)
                .lat(lat)
                .lon(lon)
                .houseGender(houseGender)
                .mainHouseImgPath(mainImgUrl.getImgUrl())
                .mainHouseImgOriginName(mainImgUrl.getOriginName())
                .mainHouseImgSaveName(mainImgUrl.getSaveName())
                .floorPlanPath(floorPlanImgUrl.getImgUrl())
                .floorPlanOriginName(floorPlanImgUrl.getOriginName())
                .floorPlanSaveName(floorPlanImgUrl.getSaveName())
                .keyWord(keyWord)
                .status(status)
                .build();
    }
}
