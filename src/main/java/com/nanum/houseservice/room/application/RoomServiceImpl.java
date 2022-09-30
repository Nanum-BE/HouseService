package com.nanum.houseservice.room.application;

import com.nanum.config.RoomStatus;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import com.nanum.houseservice.option.domain.RoomOption;
import com.nanum.houseservice.option.infrastructure.RoomOptionRepository;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.houseservice.room.domain.RoomImg;
import com.nanum.houseservice.room.domain.RoomOptionConn;
import com.nanum.houseservice.room.dto.RoomDto;
import com.nanum.houseservice.room.infrastructure.RoomImgRepository;
import com.nanum.houseservice.room.infrastructure.RoomOptionConnRepository;
import com.nanum.houseservice.room.infrastructure.RoomRepository;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HouseRepository houseRepository;
    private final S3UploaderService s3UploaderService;
    private final RoomImgRepository roomImgRepository;
    private final RoomOptionConnRepository roomOptionConnRepository;
    private final RoomOptionRepository roomOptionRepository;

    @Override
    public void createRoom(RoomDto roomDto, MultipartFile roomMainImg, List<MultipartFile> roomImgs) {
        roomDto.setStatus(RoomStatus.WAITING);
        Room room;

        try {
            S3UploadDto roomMainImgDto = new S3UploadDto();

            if(roomMainImg != null && !roomMainImg.isEmpty()) {
                roomMainImgDto = s3UploaderService.upload(List.of(roomMainImg), "roomMain").get(0);
            }

            House house = houseRepository.findById(roomDto.getHouseId()).get();
            room = roomDto.roomDtoToEntity(house, roomMainImgDto);
            room = roomRepository.save(room);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(roomDto.getRoomOption() != null) {
            try {
                for (Long roomOptionId : roomDto.getRoomOption()) {
                    RoomOption roomOption = roomOptionRepository.findById(roomOptionId).orElse(null);
                    RoomOptionConn roomOptionConn = RoomOptionConn.builder()
                            .room(room)
                            .roomOption(roomOption)
                            .build();
                    roomOptionConnRepository.save(roomOptionConn);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            List<S3UploadDto> roomImgsDto = new ArrayList<>();
            if(roomImgs != null && !roomImgs.isEmpty() && !roomImgs.get(0).isEmpty()) {
                roomImgsDto = s3UploaderService.upload(roomImgs, "room");
            }

            List<RoomImg> imgList = new ArrayList<>();
            for (S3UploadDto s3UploadDto : roomImgsDto) {
                imgList.add(s3UploadDto.roomImgToEntity(room));
            }

            roomImgRepository.saveAll(imgList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
