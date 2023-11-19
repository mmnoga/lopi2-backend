package com.liftoff.project.controller.review;

import com.liftoff.project.configuration.security.annotations.HasUserRole;
import com.liftoff.project.controller.review.request.ReviewRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-reviews")
@AllArgsConstructor
@Tag(name = "Reviews <User>")
public class ReviewAuthController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Add a new review",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasUserRole
    public ResponseEntity<ReviewResponseDTO> addReview(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {

        return ResponseEntity.ok(reviewService
                .addReview(userDetails, reviewRequestDTO));
    }

}