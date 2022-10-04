package com.nanum.houseservice.review.infrastructure;

import com.nanum.houseservice.review.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
