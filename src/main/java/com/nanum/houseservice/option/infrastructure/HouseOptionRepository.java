package com.nanum.houseservice.option.infrastructure;

import com.nanum.houseservice.option.domain.HouseOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseOptionRepository extends JpaRepository<HouseOption, Long> {
    HouseOption findByHouseOptionId(Long houseOptionId);
}
