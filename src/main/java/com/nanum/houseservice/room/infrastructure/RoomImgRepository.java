package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {
    List<RoomImg> findAllByRoomId(Long roomId);
}
