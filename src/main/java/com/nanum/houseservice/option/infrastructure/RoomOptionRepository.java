package com.nanum.houseservice.option.infrastructure;

import com.nanum.houseservice.option.domain.RoomOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomOptionRepository extends JpaRepository<RoomOption, Long> {
}
