package com.liftoff.project.repository;

import com.liftoff.project.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Retrieves a review based on its unique identifier (UUID)
     *
     * @param reviewUuid The unique identifier (UUID) of the review to retrieve
     * @return An Optional containing the review if found, or an empty Optional if not found
     */
    Optional<Review> findByUuid(UUID reviewUuid);

    /**
     * Retrieves a list of reviews associated with a specific product UUID
     *
     * @param productUuid The UUID of the product for which reviews are to be retrieved
     * @return A list of Review objects associated with the specified product UUID.
     * An empty list is returned if no reviews are found
     */
    List<Review> findReviewsByProductUuid(UUID productUuid);

    /**
     * Retrieves a paginated list of visible reviews based on the provided pagination parameters
     *
     * @param pageable The pagination information, specifying the page index and size
     * @return A Page containing visible Review objects
     */
    Page<Review> findAllByIsVisibleTrue(Pageable pageable);

    /**
     * Retrieves a paginated list of invisible reviews based on the provided pagination parameters
     *
     * @param pageable The pagination information, specifying the page index and size
     * @return A Page containing visible Review objects
     */
    Page<Review> findAllByIsVisibleFalse(Pageable pageable);

}