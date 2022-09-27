package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseImgRepository extends JpaRepository<HouseImg, Long> {
}
