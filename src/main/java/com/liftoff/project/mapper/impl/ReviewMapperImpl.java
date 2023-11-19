package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.review.request.ReviewRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.mapper.ReviewMapper;
import com.liftoff.project.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponseDTO mapReviewToReviewResponseDTO(Review review) {

        if (review == null) {
            return null;
        }

        String username = review.getUser()
                .getUsername();

        ReviewResponseDTO reviewResponseDTO = ReviewResponseDTO.builder()
                .uid(review.getUuid())
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .productUid(review.getProductUuid())
                .username(username)
                .isVisible(review.getIsVisible())
                .build();

        return reviewResponseDTO;
    }

    @Override
    public Review mapReviewRequestDTOTOReview(ReviewRequestDTO reviewRequestDTO) {

        if (reviewRequestDTO == null) {
            return null;
        }

        Review review = new Review();

        review.setRating(reviewRequestDTO.getRating());

        if (reviewRequestDTO.getReviewText() != null) {
            review.setReviewText(reviewRequestDTO.getReviewText());
        }

        return review;
    }

}