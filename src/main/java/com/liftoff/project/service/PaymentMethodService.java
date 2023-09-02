package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethodResponseDTO> getPaymentMethods();

    PaymentMethodResponseDTO addPaymentMethod(PaymentMethodRequestDTO paymentMethodRequestDTO);

    void deletePaymentMethodByName(String name);

    PaymentMethodResponseDTO editPaymentMethod(String name, PaymentMethodRequestDTO paymentMethodRequestDTO);

}
