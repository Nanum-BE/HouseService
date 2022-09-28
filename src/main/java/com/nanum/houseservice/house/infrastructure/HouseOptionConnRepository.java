package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseOptionConn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseOptionConnRepository extends JpaRepository<HouseOptionConn, Long> {
    List<HouseOptionConn> findAllByHouseHouseId(Long houseId);
}
