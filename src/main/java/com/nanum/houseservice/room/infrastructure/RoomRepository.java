package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByHouseId(Long houseId, Pageable pageable);
    boolean existsById(Long roomId);
}
