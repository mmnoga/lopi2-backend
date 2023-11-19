package com.liftoff.project.controller.review;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin-reviews")
@AllArgsConstructor
@Tag(name = "Reviews <Admin>")
public class ReviewAdminController {

    private final ReviewService reviewService;

    @DeleteMapping("/{reviewUuid}")
    @Operation(summary = "Delete a review",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<Void> deleteReview(@PathVariable @NotNull UUID reviewUuid) {

        reviewService.deleteReview(reviewUuid);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reviewUuid}/{isVisible}")
    @Operation(summary = "Set a review visibility",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<ReviewResponseDTO> setReviewVisibility(
            @PathVariable @NotNull UUID reviewUuid,
            @PathVariable @NotNull Boolean isVisible) {

        return ResponseEntity.ok(reviewService
                .setReviewVisibility(reviewUuid, isVisible));
    }

}
