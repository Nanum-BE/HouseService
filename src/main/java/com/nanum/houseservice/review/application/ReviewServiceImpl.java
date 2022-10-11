package com.nanum.houseservice.review.application;

import com.nanum.exception.CustomRunTimeException;
import com.nanum.exception.NotFoundException;
import com.nanum.houseservice.house.vo.HostHouseResponse;
import com.nanum.houseservice.review.domain.Review;
import com.nanum.houseservice.review.domain.ReviewImg;
import com.nanum.houseservice.review.dto.ReviewDto;
import com.nanum.houseservice.review.infrastructure.ReviewImgRepository;
import com.nanum.houseservice.review.infrastructure.ReviewRepository;
import com.nanum.houseservice.review.vo.ReviewImgResponse;
import com.nanum.houseservice.review.vo.ReviewResponse;
import com.nanum.houseservice.review.vo.ReviewShortResponse;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.houseservice.room.infrastructure.RoomRepository;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final S3UploaderService s3UploaderService;
    private final RoomRepository roomRepository;

    @Override
    public void createReview(ReviewDto reviewDto, List<MultipartFile> reviewImgs) {
        Room room = roomRepository.findById(reviewDto.getRoomId()).orElse(null);

        if(room == null) {
            throw new NotFoundException("해당 방 정보가 없습니다.");
        }

        Review review = reviewRepository.save(reviewDto.reviewDtoToEntity(room));

        List<S3UploadDto> reviewImgList = new ArrayList<>();
        try {
            if(reviewImgs != null && !reviewImgs.isEmpty() && !reviewImgs.get(0).isEmpty()) {
                reviewImgList = s3UploaderService.upload(reviewImgs, "review");
            }
        } catch (Exception e) {
            throw new CustomRunTimeException("S3 Upload Failed");
        }

        List<ReviewImg> reviewImgEntities = new ArrayList<>();

        for (S3UploadDto s3UploadDto : reviewImgList) {
            reviewImgEntities.add(s3UploadDto.reviewImgToEntity(review));
        }

        reviewImgRepository.saveAll(reviewImgEntities);
    }

    @Override
    public ReviewResponse retrieveReview(Long houseId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review == null) {
            throw new NotFoundException("해당 리뷰가 존재하지 않습니다.");
        }

        List<ReviewImg> reviewImgs = reviewImgRepository.findAllByReviewId(reviewId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<ReviewImgResponse> reviewImgResponses = Arrays.asList(mapper.map(reviewImgs, ReviewImgResponse[].class));

        return new ReviewResponse().entityToReviewResponse(review, reviewImgResponses);
    }

    @Override
    public Page<ReviewShortResponse> retrieveHouseReviews(Long houseId, Pageable pageable) {
        Page<Review> review = reviewRepository.findAllByRoomHouseId(houseId, pageable);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return review.map(r -> {
            ReviewImg reviewImg = reviewImgRepository.findTop1ByReviewId(r.getId());

            ReviewImgResponse reviewImgResponse = null;

            if(reviewImg != null) {
                reviewImgResponse = mapper.map(reviewImg, ReviewImgResponse.class);
            }

            return mapper.map(new ReviewShortResponse().entityToReviewShortResponse(r, reviewImgResponse), ReviewShortResponse.class);
        });
    }

    @Override
    public void updateReview(ReviewDto reviewDto, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if(review == null) {
            throw new NotFoundException("해당 리뷰가 존재하지 않습니다.");
        }

        reviewRepository.save(reviewDto.reviewUpdateDtoToEntity(review.getRoom(), reviewId));
    }

    @Override
    public void updateReviewImg(Long houseId, Long reviewId, List<Long> deleteReviewImgs, List<MultipartFile> reviewImgs) {
        if(deleteReviewImgs != null && !deleteReviewImgs.isEmpty()) {
            reviewImgRepository.deleteAllById(deleteReviewImgs);
        }

        List<S3UploadDto> reviewImgsDto = new ArrayList<>();

        try {
            if (reviewImgs != null && !reviewImgs.isEmpty() && !reviewImgs.get(0).isEmpty()) {
                reviewImgsDto = s3UploaderService.upload(reviewImgs, "review");
            }
        } catch (Exception e) {
            throw new CustomRunTimeException("Server Error");
        }

        Review review = reviewRepository.findById(reviewId).orElse(null);
        List<ReviewImg> reviewImgList = new ArrayList<>();

        if(review != null) {
            for (S3UploadDto s3UploadDto : reviewImgsDto) {
                reviewImgList.add(s3UploadDto.reviewImgToEntity(review));
            }
        }

        reviewImgRepository.saveAll(reviewImgList);
    }

    @Override
    public void deleteReview(Long houseId, Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
        }
    }
}
