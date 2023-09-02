package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.model.order.PaymentMethod;

public interface PaymentMethodMapper {

    PaymentMethodResponseDTO mapPaymentMethodToPaymentMethodResponseDTO(PaymentMethod paymentMethod);

    PaymentMethod mapPaymentMethodRequestDTOToEntity(PaymentMethodRequestDTO paymentMethodRequestDTO);

}
