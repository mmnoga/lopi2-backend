package com.liftoff.project.mapper;

import com.liftoff.project.controller.payu.response.OrderPayUCreatedResponseDTO;
import com.liftoff.project.model.OrderPayU;

public interface OrderPayUMapper {

    /**
     * Maps the OrderPayU entity to the OrderPayUCreatedResponseDTO object.
     *
     * @param orderPayU The OrderPayU entity to be mapped to the OrderPayUCreatedResponseDTO object.
     * @return The mapped OrderPayUCreatedResponseDTO object.
     */

    OrderPayUCreatedResponseDTO mapOrderPayUToOrderPayUCreatedResponseDTO(OrderPayU orderPayU);


}