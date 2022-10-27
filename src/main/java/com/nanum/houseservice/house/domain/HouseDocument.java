package com.nanum.houseservice.house.domain;

import com.nanum.config.Gender;
import com.nanum.config.HouseStatus;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

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

    @Field(type = FieldType.Keyword)
    private String houseName;

    @Field(type = FieldType.Keyword)
    private String mainHouseImgPath;

    @Field(type = FieldType.Keyword)
    private Gender houseGender;

    @Field(type = FieldType.Text, store = true,
            analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String streetAddress;

    @Field(type = FieldType.Text, store = true,
            analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String lotAddress;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text, store = true,
            analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String keyWord;

    @Field(type = FieldType.Keyword)
    private String houseType;

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
                .streetAddress(house.getStreetAddress())
                .lotAddress(house.getLotAddress())
                .houseName(house.getHouseName())
                .keyWord(house.getKeyWord())
                .houseType(house.getHouseType())
                .location(new GeoPoint(Double.parseDouble(house.getLat()), Double.parseDouble(house.getLon())))
                .houseGender(house.getHouseGender())
                .mainHouseImgPath(house.getMainHouseImgPath())
                .status(house.getStatus())
                .createdAt(house.getCreateAt())
                .updateAt(house.getUpdateAt())
                .deleteAt(house.getDeleteAt())
                .build();
    }
}