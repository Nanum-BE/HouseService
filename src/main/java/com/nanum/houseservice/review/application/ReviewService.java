package com.nanum.houseservice.review.application;

import com.nanum.houseservice.review.dto.ReviewDto;
import com.nanum.houseservice.review.vo.ReviewResponse;
import com.nanum.houseservice.review.vo.ReviewShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewDto reviewDto, List<MultipartFile> reviewImgs);
    ReviewResponse retrieveReview(Long houseId, Long reviewId);
    Page<ReviewShortResponse> retrieveHouseReviews(Long houseId, Pageable pageable);
    void updateReview(ReviewDto reviewDto, Long reviewId);
    void updateReviewImg(Long houseId, Long reviewId, List<Long> deleteReviewImgs, List<MultipartFile> reviewImgs);
    void deleteReview(Long houseId, Long reviewId);
}
