package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {

    Page<House> findAllByHostId(Long hostId, Pageable pageable);
}
