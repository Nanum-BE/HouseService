package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.dto.HouseSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    Page<House> findAllByHostId(Long hostId, Pageable pageable);

    @Query(value = "select new com.nanum.houseservice.house.dto.HouseSearch(h, max(r.monthlyRent), min(r.monthlyRent))" +
            "from House h join Room r on h.id = r.house.id " +
            "where h.streetAddress like %:keyword% or h.lotAddress like %:keyword% or h.keyWord like %:keyword% " +
            "group by r.house.id")
    List<HouseSearch> findAllBySearchWord(@Param("keyword") String keyWord);
}
