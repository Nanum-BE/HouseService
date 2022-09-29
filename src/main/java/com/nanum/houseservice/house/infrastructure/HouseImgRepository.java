package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseImgRepository extends JpaRepository<HouseImg, Long> {
    List<HouseImg> findAllByHouseIdOrderByPriorityAsc(Long houseId);
}
