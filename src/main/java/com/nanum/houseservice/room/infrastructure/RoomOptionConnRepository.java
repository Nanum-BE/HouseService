package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.RoomOptionConn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomOptionConnRepository extends JpaRepository<RoomOptionConn, Long> {
    List<RoomOptionConn> findAllByRoomId(Long roomId);
}
