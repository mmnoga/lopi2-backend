package com.liftoff.project.repository;

import com.liftoff.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds a cart by its UUID.
     *
     * @param cartUuid The UUID of the cart to find.
     * @return An {@link Optional} containing the cart if found, or an empty {@link Optional} if not found.
     */
    Optional<Cart> findByUuid(UUID cartUuid);

    /**
     * Finds a cart associated with the given user's username.
     *
     * @param username The username of the user whose cart is to be found.
     * @return An {@link Optional} containing the cart if found, or an empty {@link Optional} if not found.
     */
    Optional<Cart> findByUserUsername(String username);

}
