package com.liftoff.project.controller.review.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ReviewRequestDTO {

    @NotNull(message = "Product UUID is required")
    private UUID productUuid;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(min = 5, max = 500, message = "Review text must be between 5 and 500 characters")
    private String reviewText;

}