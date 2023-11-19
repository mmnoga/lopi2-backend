package com.liftoff.project.controller.review;

import com.liftoff.project.controller.review.response.ReviewResponseDTO;
import com.liftoff.project.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReviewAdminControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewAdminController reviewAdminController;

    @Test
    public void shouldDeleteReview() {
        // given
        UUID reviewUuid = UUID.randomUUID();

        doNothing().when(reviewService).deleteReview(reviewUuid);

        // when
        ResponseEntity<Void> responseEntity = reviewAdminController.deleteReview(reviewUuid);

        // then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(reviewService).deleteReview(reviewUuid);
    }

    @Test
    public void shouldSetReviewVisibilityToFalse() {
        // given
        UUID reviewUuid = UUID.randomUUID();

        doReturn(ReviewResponseDTO.builder().build())
                .when(reviewService).setReviewVisibility(reviewUuid, false);

        // when
        ResponseEntity<ReviewResponseDTO> responseEntity =
                reviewAdminController.setReviewVisibility(reviewUuid, false);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

}