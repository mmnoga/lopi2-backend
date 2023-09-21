package com.liftoff.project.service;

import com.liftoff.project.controller.order.response.PaymentMethodListResponseDTO;
import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;

public interface PaymentMethodService {


    /**
     * Returns DTO with list of payment method as PaymentMethodListResponseDTO.
     */
    PaymentMethodListResponseDTO getPaymentMethods();

    /**
     * Add payment method from PaymentMethodRequestDTO.
     *
     * @param paymentMethodRequestDTO The DTO of the PaymentMethod.
     */
    PaymentMethodResponseDTO addPaymentMethod(PaymentMethodRequestDTO paymentMethodRequestDTO);

    /**
     * delete payment method by name.
     *
     * @param name The String name of the PaymentMethod.
     */
    void deletePaymentMethodByName(String name);

    /**
     * Edit existed payment method object.
     *
     * @param name                     The String - the name of the PaymentMethod to be edited.
     * @param paymentMethodRequestDTO The DTO of the PaymentMethod to be edited.
     */
    PaymentMethodResponseDTO editPaymentMethod(String name, PaymentMethodRequestDTO paymentMethodRequestDTO);

}
