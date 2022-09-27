package com.nanum.houseservice.house.vo;

import com.nanum.config.HouseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostHouseResponse {

    @Schema(description = "하우스 식별자")
    private Long houseId;

    @Schema(description = "도로명 주소")
    private String lotAddress;

    @Schema(description = "하우스 이름")
    private String houseName;

    @Schema(description = "하우스 상태")
    private HouseStatus status;

    @Schema(description = "하우스 메인 사진 경로")
    private String mainHouseImgPath;

}
