package com.nanum.houseservice.wish.infrastructure;

import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.wish.domain.Wish;
import com.nanum.houseservice.wish.dto.WishIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByUserIdAndHouseId(Long userId, Long houseId);
    boolean existsByIdAndUserId(Long wishId, Long userId);
    Page<Wish> findAllByUserId(Long userId, Pageable pageable);
    Wish findByUserIdAndHouse(Long userId, House house);

    @Query(value = "select new com.nanum.houseservice.wish.dto.WishIdDto(w.id, w.house.id) from Wish w where w.userId = :userId and w.house.id in :houseIdList")
    List<WishIdDto> findWishId(@Param("userId") Long userId, @Param("houseIdList") Long[] houseIdList);
}
