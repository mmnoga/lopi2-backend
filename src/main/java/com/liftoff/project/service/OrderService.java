package com.liftoff.project.service;

import java.util.UUID;

public interface OrderService {

    /**
     * Add or edit order from existed cart.
     *
     * @param cartUuid The UUID of the order to be added.
     */
    void processOrder(UUID cartUuid);


}