package com.nanum.houseservice.wish.infrastructure;

import com.nanum.houseservice.wish.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByUserIdAndHouseId(Long userId, Long houseId);
    boolean existsByIdAndUserId(Long wishId, Long userId);
    Page<Wish> findAllByUserId(Long userId, Pageable pageable);
}
