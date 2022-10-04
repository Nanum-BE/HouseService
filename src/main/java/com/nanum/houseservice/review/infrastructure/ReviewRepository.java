package com.nanum.houseservice.review.infrastructure;

import com.nanum.houseservice.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
