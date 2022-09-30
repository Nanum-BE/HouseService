package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
