package com.liftoff.project.mapper;

import com.liftoff.project.controller.review.request.ReviewRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.model.Review;

public interface ReviewMapper {

    /**
     * Maps a Review entity to a ReviewResponseDTO data transfer object
     *
     * @param review The Review entity to be mapped
     * @return The mapped ReviewResponseDTO
     */
    ReviewResponseDTO mapReviewToReviewResponseDTO(Review review);

    /**
     * Maps a ReviewRequestDTO data transfer object to a Review entity
     *
     * @param reviewRequestDTO The ReviewRequestDTO to be mapped
     * @return The mapped Review entity
     */
    Review mapReviewRequestDTOTOReview(ReviewRequestDTO reviewRequestDTO);

}