package com.liftoff.project.repository;

import com.liftoff.project.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {


    /**
     * Finds a order by its UUID.
     *
     * @param orderUuid The UUID of the order to find.
     * @return An {@link Optional} containing the order if found, or an empty {@link Optional} if not found.
     */
    Optional<Order> findByUuid(UUID orderUuid);

    /**
     * Finds a order associated with the given user's username.
     *
     * @param username The username of the user whose order is to be found.
     * @return An {@link Optional} containing the order if found, or an empty {@link Optional} if not found.
     */
    Optional<Order> findByUserUsername(String username);



    /**
     * Finds a order by Cart UUID.
     *
     * @param cartUuid The UUID of the cart.
     * @return An {@link Optional} containing the order if found, or an empty {@link Optional} if not found.
     */

    Optional<Order> findByCartUuid(UUID cartUuid);
}
