package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

    List<House> findAllByHostId(Long hostId);
}
