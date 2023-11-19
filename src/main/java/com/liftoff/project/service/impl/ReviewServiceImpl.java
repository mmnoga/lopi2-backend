package com.liftoff.project.service.impl;

import com.liftoff.project.controller.review.request.PaginationParameterRequestDTO;
import com.liftoff.project.controller.review.request.ReviewRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.ReviewMapper;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.Review;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.ReviewRepository;
import com.liftoff.project.service.ProductService;
import com.liftoff.project.service.ReviewService;
import com.liftoff.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public List<ReviewResponseDTO> getReviewsForProduct(UUID productUuid) {

        Product product = productService
                .getProductEntityByUuid(productUuid);

        List<Review> reviews = reviewRepository
                .findReviewsByProductUuid(product.getUId());

        return reviews.stream()
                .map(reviewMapper::mapReviewToReviewResponseDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO addReview(
            UserDetails userDetails,
            ReviewRequestDTO reviewRequestDTO) {

        UUID productUuid = reviewRequestDTO.getProductUuid();
        Product product = productService
                .getProductEntityByUuid(productUuid);

        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        Review review = reviewMapper
                .mapReviewRequestDTOTOReview(reviewRequestDTO);

        review.setUuid(UUID.randomUUID());
        review.setProductUuid(product.getUId());
        review.setUser(user);
        review.setIsVisible(true);

        Review savedReview = reviewRepository.save(review);

        return reviewMapper
                .mapReviewToReviewResponseDTO(savedReview);
    }

    @Override
    public Review getReviewByUid(UUID reviewUuid) {

        return reviewRepository
                .findByUuid(reviewUuid)
                .orElseThrow(() ->
                        new BusinessException("Review with UUID: " + reviewUuid + " not found",
                                HttpStatus.BAD_REQUEST));
    }

    @Override
    public List<ReviewResponseDTO> getPaginatedReviews(PaginationParameterRequestDTO paginationParameters) {

        int page = paginationParameters.getPageIndex();
        int size = paginationParameters.getPageSize();
        Boolean isVisible = paginationParameters.getIsVisible();

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviewPage;

        reviewPage = (isVisible != null && isVisible) ? reviewRepository.findAllByIsVisibleTrue(pageable) :
                (isVisible != null) ? reviewRepository.findAllByIsVisibleFalse(pageable) :
                        reviewRepository.findAll(pageable);

        return reviewPage.getContent().stream()
                .map(reviewMapper::mapReviewToReviewResponseDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO setReviewVisibility(UUID reviewUuid, Boolean isVisible) {

        Review review = reviewRepository
                .findByUuid(reviewUuid)
                .orElseThrow(() ->
                        new BusinessException("Review with UUID: " + reviewUuid + " not found",
                                HttpStatus.BAD_REQUEST));

        review.setIsVisible(isVisible);

        Review savedReview = reviewRepository.save(review);

        return reviewMapper
                .mapReviewToReviewResponseDTO(savedReview);
    }

    @Override
    public void deleteReview(UUID reviewUuid) {

        Review review = this.getReviewByUid(reviewUuid);

        reviewRepository.delete(review);
    }

}