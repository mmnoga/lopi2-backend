package com.liftoff.project.controller.review;

import com.liftoff.project.controller.review.request.ReviewRequestDTO;
import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReviewAuthControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewAuthController reviewAuthController;

    @Test
    public void shouldAddReview() {
        // given
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "username",
                "password",
                new ArrayList<>());

        ReviewRequestDTO reviewRequestDTO = ReviewRequestDTO.builder().build();
        ReviewResponseDTO mockReviewResponseDTO = ReviewResponseDTO.builder().build();

        when(reviewService.addReview(userDetails, reviewRequestDTO)).thenReturn(mockReviewResponseDTO);

        // when
        ResponseEntity<ReviewResponseDTO> responseEntity =
                reviewAuthController.addReview(userDetails, reviewRequestDTO);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockReviewResponseDTO, responseEntity.getBody());
    }

}