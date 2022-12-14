package com.nanum.houseservice.room.application;

import com.nanum.houseservice.room.dto.RoomDto;
import com.nanum.houseservice.room.vo.HostRoomResponse;
import com.nanum.houseservice.room.vo.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    void createRoom(RoomDto roomDto, MultipartFile roomMainImg, List<MultipartFile> roomImgs);
    Page<RoomResponse> retrieveHostAllRooms(Long houseId, Pageable pageable);
    HostRoomResponse retrieveHostRoom(Long houseId, Long roomId);
    void updateRoom(Long houseId, Long roomId, RoomDto roomDto, MultipartFile roomMainImg);
    void updateRoomImg(Long houseId, Long roomId, List<Long> deleteRoomImgs, List<MultipartFile> roomImgs);
    void deleteRoom(Long houseId, Long roomId);
}
