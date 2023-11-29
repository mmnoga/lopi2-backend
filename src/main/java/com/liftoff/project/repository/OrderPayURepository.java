package com.liftoff.project.repository;


import com.liftoff.project.model.OrderPayU;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderPayURepository extends JpaRepository<OrderPayU, Long> {


    /**
     * Finds a OrderPayU by its UUID.
     *
     * @param orderPayUUuid The UUID of the order to find.
     * @return An {@link Optional} containing the order if found, or an empty {@link Optional} if not found.
     */
    Optional<OrderPayU> findByUuid(UUID orderPayUUuid);

    /**
     * Finds a OrderPayU by its orderId.
     *
     * @param orderId The orderId of the orderPayU to find.
     * @return An {@link Optional} containing the orderPayU if found, or an empty {@link Optional} if not found.
     */
    Optional<OrderPayU> findByOrderId(String orderId);

    /**
     * Finds a OrderPayU by its extOrderId.
     *
     * @param extOrderId The extOrderId of the orderPayU to find.
     * @return An {@link Optional} containing the order if found, or an empty {@link Optional} if not found.
     */
    Optional<OrderPayU> findByExtOrderId(String extOrderId);


}
