package com.nanum.houseservice.review.infrastructure;

import com.nanum.houseservice.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByRoomHouseId(Long houseId, Pageable pageable);
}
