package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseFileRepository extends JpaRepository<HouseFile, Long> {
    HouseFile findByHouseId(Long houseId);
}
