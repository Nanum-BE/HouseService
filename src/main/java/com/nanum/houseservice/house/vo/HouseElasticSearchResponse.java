package com.nanum.houseservice.house.vo;

import com.nanum.config.Gender;
import com.nanum.houseservice.house.domain.HouseDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.elasticsearch.core.SearchHit;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseElasticSearchResponse {
    @Schema(description = "하우스 식별자")
    private Long id;

    @Schema(description = "하우스 이름")
    private String houseName;

    @Schema(description = "하우스 메인 사진 URL")
    private String mainHouseImgPath;

    @Schema(description = "하우스 성별 타입")
    private Gender houseGender;

    @Schema(description = "하우스 도로명 주소")
    private String streetAddress;

    @Schema(description = "하우스 지번 주소")
    private String lotAddress;

    @Schema(description = "위도")
    private String lat;

    @Schema(description = "경도")
    private String lon;

    public static HouseElasticSearchResponse from(SearchHit<HouseDocument> searchHit) {
        return HouseElasticSearchResponse.builder()
                .id(searchHit.getContent().getId())
                .houseName(searchHit.getContent().getHouseName())
                .mainHouseImgPath(searchHit.getContent().getMainHouseImgPath())
                .houseGender(searchHit.getContent().getHouseGender())
                .streetAddress(searchHit.getContent().getStreetAddress())
                .lotAddress(searchHit.getContent().getLotAddress())
                .lat(String.valueOf(searchHit.getContent().getLocation().getLat()))
                .lon(String.valueOf(searchHit.getContent().getLocation().getLon()))
                .build();
    }

    public static HouseElasticSearchResponse fromDoc(HouseDocument houseDocument) {
        return HouseElasticSearchResponse.builder()
                .id(houseDocument.getId())
                .houseName(houseDocument.getHouseName())
                .mainHouseImgPath(houseDocument.getMainHouseImgPath())
                .houseGender(houseDocument.getHouseGender())
                .streetAddress(houseDocument.getStreetAddress())
                .lotAddress(houseDocument.getLotAddress())
                .lat(String.valueOf(houseDocument.getLocation().getLat()))
                .lon(String.valueOf(houseDocument.getLocation().getLon()))
                .build();
    }
}
