package com.liftoff.project.controller.review.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ReviewResponseDTO {

    private UUID uid;
    private UUID productUid;
    private int rating;
    private String reviewText;
    private String username;
    private boolean isVisible;

}