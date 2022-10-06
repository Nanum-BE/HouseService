package com.nanum.houseservice.house.domain;

import com.nanum.config.Gender;
import com.nanum.config.HouseStatus;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;
import java.time.LocalDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Mapping(mappingPath = "house-mapping.json")
@Setting(settingPath = "house-setting.json")
@Document(indexName = "house")
public class HouseDocument {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Long)
    private Long hostId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String streetAddress;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String lotAddress;

    @Field(type = FieldType.Keyword)
    private String zipCode;

    @Field(type = FieldType.Text)
    private String explanation;

    @Field(type = FieldType.Text, store = true,
            analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String houseName;

    @Field(type = FieldType.Keyword)
    private String houseType;

    @Field(type = FieldType.Keyword)
    private String lat;

    @Field(type = FieldType.Keyword)
    private String lon;

    @Field(type = FieldType.Keyword)
    private Gender houseGender;

    @Field(type = FieldType.Keyword)
    private String mainHouseImgPath;
    @Field(type = FieldType.Keyword)
    private String mainHouseImgOriginName;
    @Field(type = FieldType.Keyword)
    private String mainHouseImgSaveName;

    @Field(type = FieldType.Keyword)
    private String floorPlanPath;
    @Field(type = FieldType.Keyword)
    private String floorPlanOriginName;
    @Field(type = FieldType.Keyword)
    private String floorPlanSaveName;

    @Field(type = FieldType.Keyword)
    private HouseStatus status;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;
    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime updateAt;
    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime deleteAt;

    public static HouseDocument from(House house) {
        return HouseDocument.builder()
                .id(house.getId())
                .hostId(house.getHostId())
                .streetAddress(house.getStreetAddress())
                .zipCode(house.getZipCode())
                .explanation(house.getExplanation())
                .houseName(house.getHouseName())
                .houseType(house.getHouseType())
                .lat(house.getLat())
                .lon(house.getLon())
                .houseGender(house.getHouseGender())
                .mainHouseImgPath(house.getMainHouseImgPath())
                .mainHouseImgOriginName(house.getMainHouseImgOriginName())
                .mainHouseImgSaveName(house.getMainHouseImgSaveName())
                .floorPlanPath(house.getFloorPlanPath())
                .floorPlanOriginName(house.getFloorPlanOriginName())
                .floorPlanSaveName(house.getFloorPlanSaveName())
                .status(house.getStatus())
                .createdAt(house.getCreateAt())
                .updateAt(house.getUpdateAt())
                .deleteAt(house.getDeleteAt())
                .build();
    }
}
