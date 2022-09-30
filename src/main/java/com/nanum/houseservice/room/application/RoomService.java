package com.nanum.houseservice.room.application;

import com.nanum.houseservice.room.dto.RoomDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    void createRoom(RoomDto roomDto, MultipartFile roomMainImg, List<MultipartFile> roomImgs);
}
