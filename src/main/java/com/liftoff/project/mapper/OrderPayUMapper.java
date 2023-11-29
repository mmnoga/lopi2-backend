package com.liftoff.project.mapper;

import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.model.OrderPayU;

public interface OrderPayUMapper {

    /**
     * Maps the OrderPayU entity to the OrderPayUCreatedResponseDTO object.
     *
     * @param orderPayU The OrderPayU entity to be mapped to the OrderPayUCreatedResponseDTO object.
     * @return The mapped OrderPayUCreatedResponseDTO object.
     */

    OrderCreatedResponseDTO mapOrderToOrderResponseDTO(OrderPayU orderPayU);

    /**
     * Maps the OrderPayUCreatedResponseDTO object to the OrderPayU entity.
     *
     * @param orderResponseDTO The OrderPayUCreatedResponseDTO object to be mapped to OrderPayU entity.
     * @return The mapped OrderPayU entity object.
     */

     OrderPayU mapOrderResponseDTOTOrderPayU(OrderResponseDTO orderResponseDTO);
}