package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.model.order.PaymentMethod;

public interface PaymentMethodMapper {


    /**
     * Maps the PaymentMethod entity to the PaymentMethodResponseDTO object.
     *
     * @param paymentMethod The PaymentMethod entity to be mapped to the PaymentMethodResponseDTO object.
     * @return The mapped PaymentMethodResponseDTO object.
     */
    PaymentMethodResponseDTO mapPaymentMethodToPaymentMethodResponseDTO(PaymentMethod paymentMethod);

    /**
     * Maps the PaymentMethodRequestDTO object to the PaymentMethod entity.
     *
     * @param paymentMethodRequestDTO The PaymentMethodRequestDTO object to be mapped to the PaymentMethod entity.
     * @return The mapped PaymentMethod entity.
     */
    PaymentMethod mapPaymentMethodRequestDTOToEntity(PaymentMethodRequestDTO paymentMethodRequestDTO);

}
