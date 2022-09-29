package com.nanum.houseservice.option.infrastructure;

import com.nanum.houseservice.option.domain.HouseOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseOptionRepository extends JpaRepository<HouseOption, Long> {
    Optional<HouseOption> findById(Long id);
}
