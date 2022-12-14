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

    List<House> findTop10ByOrderByUpdateAtDesc();

    List<House> findTop10ByHouseTypeOrderByUpdateAtDesc(String houseType);

    @Query(value = "select new com.nanum.houseservice.house.dto.HouseSearch(h, max(r.monthlyRent), min(r.monthlyRent), count(distinct w), count(distinct re.id), avg(re.score)) " +
            "from House h join Room r on h.id = r.house.id left outer join Review re on r.id = re.room.id left outer join Wish w on h.id = w.house.id " +
            "where h.streetAddress like %:keyword% or h.lotAddress like %:keyword% or h.keyWord like %:keyword% and w.deleteAt is null and re.deleteAt is null " +
            "group by r.house.id")
    List<HouseSearch> findAllBySearchWord(@Param("keyword") String keyWord);

    @Query(value = "select new com.nanum.houseservice.house.dto.HouseSearch(h, max(r.monthlyRent), min(r.monthlyRent), count(distinct w), count(distinct re.id), avg(re.score)) " +
            "from House h join Room r on h.id = r.house.id left outer join Review re on r.id = re.room.id left outer join Wish w on h.id = w.house.id " +
            "where w.deleteAt is null and re.deleteAt is null " +
            "group by r.house.id")
    List<HouseSearch> findAllBySearch();

    @Query(value = "select new com.nanum.houseservice.house.dto.HouseSearch(h, max(r.monthlyRent), min(r.monthlyRent), count(distinct w), count(distinct re.id), avg(re.score)) " +
            "from House h join Room r on h.id = r.house.id left outer join Review re on r.id = re.room.id left outer join Wish w on h.id = w.house.id " +
            "where h.id =:houseId and w.deleteAt is null and re.deleteAt is null")
    HouseSearch findTotal(@Param("houseId") Long houseId);

    @Query(value = "select new com.nanum.houseservice.house.dto.HouseSearch(h, max(r.monthlyRent), min(r.monthlyRent), count(distinct w), count(distinct re.id), avg(re.score)) " +
            "from House h join Room r on h.id = r.house.id left outer join Review re on r.id = re.room.id left outer join Wish w on h.id = w.house.id " +
            "where h.id in :houseIdList and w.deleteAt is null and re.deleteAt is null " +
            "group by h.id")
    List<HouseSearch> findTotalList(@Param("houseIdList") List<Long> houseIdList);
}
