package com.nanum.houseservice.room.vo;

import com.nanum.config.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {

    @NotNull(message = "hostId cannot be null")
    @Schema(description = "호스트 식별자", defaultValue = "1")
    private Long hostId;

    @NotNull(message = "roomGender cannot be null")
    @Schema(description = "방 성별", defaultValue = "COMMON")
    private Gender roomGender;

    @NotNull(message = "area cannot be null")
    @Schema(description = "방 면적", defaultValue = "4.4㎡")
    private String area;

    @NotNull(message = "name cannot be null")
    @Schema(description = "방 이름 또는 방 호수", defaultValue = "A101")
    private String name;

    @NotNull(message = "totalMember cannot be null")
    @Schema(description = "방 인원 수", defaultValue = "2")
    private int totalMember;

    @NotNull(message = "deposit cannot be null")
    @Schema(description = "보증금", defaultValue = "1130000")
    private int deposit;

    @NotNull(message = "monthlyRent cannot be null")
    @Schema(description = "월 임대료", defaultValue = "465000")
    private int monthlyRent;

    @NotNull(message = "maintenanceFee cannot be null")
    @Schema(description = "관리비", defaultValue = "40000")
    private int maintenanceFee;

    @NotNull(message = "prepaidUtilityBill cannot be null")
    @Schema(description = "선불공과금", defaultValue = "50000")
    private int prepaidUtilityBill;

    @Schema(description = "방 옵션")
    private List<Long> roomOption;
}
