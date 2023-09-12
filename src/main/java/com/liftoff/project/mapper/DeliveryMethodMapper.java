package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.model.order.DeliveryMethod;

public interface DeliveryMethodMapper {

    /**
     * Maps the DeliveryMethod entity to the DeliveryMethodResponseDTO object.
     *
     * @param deliveryMethod The DeliveryMethod entity to be mapped to the DeliveryMethodResponseDTO object.
     * @return The mapped DeliveryMethodResponseDTO object.
     */
    DeliveryMethodResponseDTO mapDeliveryMethodToDeliveryMethodResponseDTO(DeliveryMethod deliveryMethod);


    /**
     * Maps the DeliveryMethodRequestDTO object to the DeliveryMethod entity.
     *
     * @param deliveryMethodRequestDTO The DeliveryMethodRequestDTO object to be mapped to the DeliveryMethod entity.
     * @return The mapped DeliveryMethod entity.
     */
    DeliveryMethod mapDeliveryMethodRequestDTOToEntity(DeliveryMethodRequestDTO deliveryMethodRequestDTO);

}
