package com.liftoff.project.controller.review;

import com.liftoff.project.controller.review.request.PaginationParameterRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{productUuid}")
    @Operation(summary = "Get a list of reviews for a product")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsForProduct(
            @PathVariable @NotNull UUID productUuid) {

        return ResponseEntity.ok(reviewService
                .getReviewsForProduct(productUuid));
    }

    @GetMapping
    @Operation(summary = "Get a paginated list of reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getPaginatedReviews(
            @Parameter(description = "pageIndex: Page index, pageSize: Page size, " +
                    "isVisible: Flag indicating whether to include visible reviews (true) or invisible " +
                    "reviews (false).  If not provided, all reviews will be included.")
            @Valid @RequestBody PaginationParameterRequestDTO paginationParameters) {

        return ResponseEntity.ok(reviewService
                .getPaginatedReviews(paginationParameters));
    }

}