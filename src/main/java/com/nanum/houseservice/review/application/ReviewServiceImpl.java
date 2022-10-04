package com.nanum.houseservice.review.application;

import com.nanum.exception.CustomRunTimeException;
import com.nanum.exception.NotFoundException;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import com.nanum.houseservice.review.domain.Review;
import com.nanum.houseservice.review.domain.ReviewImg;
import com.nanum.houseservice.review.dto.ReviewDto;
import com.nanum.houseservice.review.infrastructure.ReviewImgRepository;
import com.nanum.houseservice.review.infrastructure.ReviewRepository;
import com.nanum.util.s3.S3UploadDto;
import com.nanum.util.s3.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final S3UploaderService s3UploaderService;
    private final HouseRepository houseRepository;

    @Override
    public void createReview(ReviewDto reviewDto, List<MultipartFile> reviewImgs) {
        House house = houseRepository.findById(reviewDto.getHouseId()).orElse(null);

        if(house == null) {
            throw new NotFoundException("해당 하우스 정보가 없습니다.");
        }
        Review review = reviewRepository.save(reviewDto.reviewDtoToEntity(house));

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
}
