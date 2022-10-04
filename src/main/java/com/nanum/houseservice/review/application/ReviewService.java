package com.nanum.houseservice.review.application;

import com.nanum.houseservice.review.dto.ReviewDto;
import com.nanum.houseservice.review.vo.ReviewResponse;
import com.nanum.houseservice.review.vo.ReviewShortResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewDto reviewDto, List<MultipartFile> reviewImgs);
    ReviewResponse retrieveReview(Long houseId, Long reviewId);
    List<ReviewShortResponse> retrieveHouseReviews(Long houseId);
    void updateReview(ReviewDto reviewDto, Long reviewId);
}
