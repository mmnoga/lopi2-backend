package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.model.order.DeliveryMethod;

public interface DeliveryMethodMapper {

    DeliveryMethodResponseDTO mapDeliveryMethodToDeliveryMethodResponseDTO(DeliveryMethod deliveryMethod);

    DeliveryMethod mapDeliveryMethodRequestDTOToEntity(DeliveryMethodRequestDTO deliveryMethodRequestDTO);

}
