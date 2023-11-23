package com.liftoff.project.service.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderPayUCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.service.PayUService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayUServiceImpl implements PayUService {

    private final PayUApiClient payUApiClient;

    @Override
    public PayUAuthResponseDTO getAccessToken() {
        return payUApiClient
                .getAccessToken();
    }

    @Override
    public PaymentMethodResponseDTO getPaymentMethods(String authorizationHeader) {
        return payUApiClient
                .getPaymentMethods(authorizationHeader);
    }

    @Override
    public OrderPayUCreatedResponseDTO submitOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO) {
        return payUApiClient
                .submitOrder(authorizationHeader, orderCreateRequestDTO);
    }

}