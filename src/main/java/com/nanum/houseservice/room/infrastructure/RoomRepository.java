package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByHouseId(Long houseId);
    boolean existsById(Long roomId);
}
