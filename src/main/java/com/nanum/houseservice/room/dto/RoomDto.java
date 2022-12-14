package com.nanum.houseservice.room.dto;

import com.nanum.config.Gender;
import com.nanum.config.RoomStatus;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.util.s3.S3UploadDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDto {
    private Long hostId;
    private Long houseId;
    private Gender roomGender;
    private String area;
    private String name;
    private int totalMember;
    private int deposit;
    private int monthlyRent;
    private int maintenanceFee;
    private int prepaidUtilityBill;
    private LocalDate contractEndAt;
    private RoomStatus status;
    private String mainRoomImgPath;
    private List<Long> roomOption;

    public Room roomDtoToEntity(House house, S3UploadDto roomMainImgDto) {
        return Room.builder()
                .house(house)
                .roomGender(roomGender)
                .area(area)
                .name(name)
                .totalMember(totalMember)
                .deposit(deposit)
                .monthlyRent(monthlyRent)
                .maintenanceFee(maintenanceFee)
                .prepaidUtilityBill(prepaidUtilityBill)
                .contractEndAt(null)
                .status(status)
                .mainRoomImgPath(roomMainImgDto.getImgUrl())
                .mainRoomImgOriginName(roomMainImgDto.getOriginName())
                .mainRoomImgSaveName(roomMainImgDto.getSaveName())
                .build();
    }

    public Room updateRoomDtoToEntity(House house, S3UploadDto roomMainImgDto, Long roomId) {
        return Room.builder()
                .id(roomId)
                .house(house)
                .roomGender(roomGender)
                .area(area)
                .name(name)
                .totalMember(totalMember)
                .deposit(deposit)
                .monthlyRent(monthlyRent)
                .maintenanceFee(maintenanceFee)
                .prepaidUtilityBill(prepaidUtilityBill)
                .contractEndAt(contractEndAt)
                .status(status)
                .mainRoomImgPath(roomMainImgDto.getImgUrl())
                .mainRoomImgOriginName(roomMainImgDto.getOriginName())
                .mainRoomImgSaveName(roomMainImgDto.getSaveName())
                .build();
    }
}
