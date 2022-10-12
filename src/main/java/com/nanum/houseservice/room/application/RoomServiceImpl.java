package com.nanum.houseservice.room.application;

import com.nanum.config.RoomStatus;
import com.nanum.exception.CustomRunTimeException;
import com.nanum.exception.NotFoundException;
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
import com.nanum.houseservice.room.vo.HostRoomResponse;
import com.nanum.houseservice.room.vo.RoomImgResponse;
import com.nanum.houseservice.room.vo.RoomOptionConnResponse;
import com.nanum.houseservice.room.vo.RoomResponse;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
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
        Room room = null;

        try {
            S3UploadDto roomMainImgDto = new S3UploadDto();

            if(roomMainImg != null && !roomMainImg.isEmpty()) {
                roomMainImgDto = s3UploaderService.upload(List.of(roomMainImg), "roomMain").get(0);
            }

            House house = houseRepository.findById(roomDto.getHouseId()).orElse(null);

            if(house != null) {
                room = roomDto.roomDtoToEntity(house, roomMainImgDto);
                room = roomRepository.save(room);
            }

        } catch (Exception e) {
            throw new CustomRunTimeException("Server Error");
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
                throw new CustomRunTimeException("Server Error");
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
            throw new CustomRunTimeException("Server Error");
        }
    }

    @Override
    public Page<RoomResponse> retrieveHostAllRooms(Long houseId, Pageable pageable) {

        Page<Room> rooms = roomRepository.findAllByHouseId(houseId, pageable);

        if(rooms.isEmpty()) {
            throw new NotFoundException(String.format("No Lookup Value"));
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return rooms.map(room -> {
            return mapper.map(room, RoomResponse.class);
        });
    }

    @Override
    public HostRoomResponse retrieveHostRoom(Long houseId, Long roomId) {
        Room room = roomRepository.findById(roomId).get();

        List<RoomImg> roomImgs = roomImgRepository.findAllByRoomId(roomId);
        List<RoomOptionConn> roomOptionConns = roomOptionConnRepository.findAllByRoomId(roomId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RoomResponse roomResponse = mapper.map(room, RoomResponse.class);
        List<RoomImgResponse> roomImgResponses = Arrays.asList(mapper.map(roomImgs, RoomImgResponse[].class));
        List<RoomOptionConnResponse> roomOptionConnResponses = new ArrayList<>();

        roomOptionConns.forEach(h -> roomOptionConnResponses.add(RoomOptionConnResponse.builder()
                .id(h.getId())
                .optionName(h.getRoomOption().getOptionName())
                .iconPath(h.getRoomOption().getIconPath())
                .build()));

        return HostRoomResponse.builder()
                .room(roomResponse)
                .roomImgs(roomImgResponses)
                .roomOptionConn(roomOptionConnResponses)
                .build();
    }

    @Override
    public void updateRoom(Long houseId, Long roomId,
                           RoomDto roomDto, MultipartFile roomMainImg) {

        Room room = roomRepository.findById(roomId).get();
        roomDto.setStatus(room.getStatus());
        roomDto.setContractEndAt(room.getContractEndAt());

        S3UploadDto roomMainImgDto;

        try {
            if(roomMainImg != null && !roomMainImg.isEmpty()) {
                roomMainImgDto = s3UploaderService.upload(List.of(roomMainImg), "room").get(0);
            } else {
                roomMainImgDto = S3UploadDto.builder()
                        .originName(room.getMainRoomImgOriginName())
                        .saveName(room.getMainRoomImgSaveName())
                        .imgUrl(room.getMainRoomImgPath())
                        .build();
            }
        } catch (Exception e) {
            throw new CustomRunTimeException("이미지 추가 에러");
        }

        Room newRoom = roomDto.updateRoomDtoToEntity(room.getHouse(), roomMainImgDto, roomId);
        roomRepository.save(newRoom);

        try {
            List<RoomOptionConn> roomOptionConns = roomOptionConnRepository.findAllByRoomId(roomId);
            roomOptionConnRepository.deleteAll(roomOptionConns);

            if(roomDto.getRoomOption().size() > 0) {
                List<RoomOption> roomOptions = roomOptionRepository.findAllById(roomDto.getRoomOption());
                List<RoomOptionConn> roomOptionConnList = new ArrayList<>();

                roomOptions.forEach(roomOption -> roomOptionConnList.add(
                        RoomOptionConn.builder()
                                .room(room)
                                .roomOption(roomOption)
                                .build()
                ));
                roomOptionConnRepository.saveAll(roomOptionConnList);
            }
        } catch (Exception e) {
            throw new CustomRunTimeException("옵션 변경 에러");
        }
    }

    @Override
    public void updateRoomImg(Long houseId, Long roomId, List<Long> deleteRoomImgs, List<MultipartFile> roomImgs) {
        if(deleteRoomImgs != null && !deleteRoomImgs.isEmpty()) {
            roomImgRepository.deleteAllById(deleteRoomImgs);
        }

        List<S3UploadDto> roomImgsDto = new ArrayList<>();

        try {
            if(roomImgs != null && !roomImgs.isEmpty() && !roomImgs.get(0).isEmpty()) {
                roomImgsDto = s3UploaderService.upload(roomImgs, "room");
            }
        } catch (Exception e) {
            throw new CustomRunTimeException("Server Error");
        }

        Room room = roomRepository.findById(roomId).orElse(null);
        List<RoomImg> roomImgList = new ArrayList<>();

        if(room != null) {
            for (S3UploadDto s3UploadDto : roomImgsDto) {
                roomImgList.add(s3UploadDto.roomImgToEntity(room));
            }
        }

        roomImgRepository.saveAll(roomImgList);
    }

    @Override
    public void deleteRoom(Long houseId, Long roomId) {
        if(roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
        }
    }
}
