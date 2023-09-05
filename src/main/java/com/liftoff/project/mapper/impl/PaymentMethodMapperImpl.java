package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.mapper.PaymentMethodMapper;
import com.liftoff.project.model.order.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapperImpl implements PaymentMethodMapper {
    @Override
    public PaymentMethodResponseDTO mapPaymentMethodToPaymentMethodResponseDTO(PaymentMethod paymentMethod) {

        if (paymentMethod == null) return null;
        return PaymentMethodResponseDTO.builder()
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .build();
    }

    @Override
    public PaymentMethod mapPaymentMethodRequestDTOToEntity(PaymentMethodRequestDTO paymentMethodRequestDTO) {

        if (paymentMethodRequestDTO == null) return null;
        return PaymentMethod.builder()
                .name(paymentMethodRequestDTO.getName())
                .description(paymentMethodRequestDTO.getDescription())
                .build();
    }

}