package com.nanum.houseservice.room.infrastructure;

import com.nanum.houseservice.room.domain.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {
}
