package com.nanum.houseservice.review.infrastructure;

import com.nanum.houseservice.review.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findAllByReviewId(Long reviewId);
    ReviewImg findTop1ByReviewId(Long reviewId);
}
