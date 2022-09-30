package com.nanum.houseservice.house.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseFileResponse {

    @Schema(description = "하우스 서류 식별자")
    private Long id;

    @Schema(description = "하우스 서류 경로")
    private String filePath;
}
