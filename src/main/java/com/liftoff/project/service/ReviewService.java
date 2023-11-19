package com.liftoff.project.service;

import com.liftoff.project.controller.review.request.PaginationParameterRequestDTO;
import com.liftoff.project.controller.review.request.ReviewRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.Review;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    /**
     * Retrieves a list of reviews
     *
     * @return A list of ReviewResponseDTO representing the reviews
     */
    List<ReviewResponseDTO> getReviewsForProduct(UUID productUuid);

    /**
     * Adds a new review based on the provided user details and review request
     *
     * @param userDetails      The user details associated with the user adding the review
     * @param reviewRequestDTO The data transfer object containing the details of the review to be added
     * @return A {@code ReviewResponseDTO} representing the added review
     * @throws BusinessException If there is an issue adding the review, a BusinessException is thrown
     *                           with details about the error
     */
    ReviewResponseDTO addReview(
            UserDetails userDetails,
            ReviewRequestDTO reviewRequestDTO);

    /**
     * Deletes a review based on the provided unique identifier (UUID)
     *
     * @param reviewUuid The unique identifier (UUID) of the review to be deleted
     * @throws BusinessException If there is an issue deleting the review, a BusinessException is thrown
     *                           with details about the error
     */
    void deleteReview(UUID reviewUuid);

    /**
     * Retrieves a review based on its unique identifier (UUID)
     *
     * @param reviewUuid The unique identifier (UUID) of the review to retrieve
     * @return The review with the specified UUID, if found
     * @throws BusinessException If the review with the given UUID is not found, a BusinessException is thrown.
     *                           The exception message contains information about the non-existent review
     */
    Review getReviewByUid(UUID reviewUuid);

    /**
     * Retrieves a paginated list of reviews based on the provided pagination parameters
     *
     * @param paginationParameters The pagination parameters specifying the page index, page size,
     *                             and visibility of reviews
     * @return A list of ReviewResponseDTO objects representing the paginated reviews
     */
    List<ReviewResponseDTO> getPaginatedReviews(PaginationParameterRequestDTO paginationParameters);

    /**
     * Sets the visibility of a review identified by its UUID.
     *
     * @param reviewUuid The UUID of the review to be updated
     * @param isVisible  The visibility status to be set (true for visible, false for invisible)
     * @return A ReviewResponseDTO representing the updated review
     * @throws BusinessException If the review with the specified UUID is not found,
     *                           a BusinessException is thrown with an appropriate error message
     *                           and HttpStatus.BAD_REQUEST
     */
    ReviewResponseDTO setReviewVisibility(UUID reviewUuid, Boolean isVisible);

}