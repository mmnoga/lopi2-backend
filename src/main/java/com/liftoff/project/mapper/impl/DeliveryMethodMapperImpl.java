package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.mapper.DeliveryMethodMapper;
import com.liftoff.project.model.order.DeliveryMethod;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMethodMapperImpl implements DeliveryMethodMapper {
    @Override
    public DeliveryMethodResponseDTO mapDeliveryMethodToDeliveryMethodResponseDTO(DeliveryMethod deliveryMethod) {
        return DeliveryMethodResponseDTO.builder()
                .name(deliveryMethod.getName())
                .description(deliveryMethod.getDescription())
                .cost(deliveryMethod.getCost())
                .build();
    }

    @Override
    public DeliveryMethod mapDeliveryMethodRequestDTOToEntity(DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        return DeliveryMethod.builder()
                .name(deliveryMethodRequestDTO.getName())
                .description(deliveryMethodRequestDTO.getDescription())
                .cost(deliveryMethodRequestDTO.getCost())
                .build();
    }

}
