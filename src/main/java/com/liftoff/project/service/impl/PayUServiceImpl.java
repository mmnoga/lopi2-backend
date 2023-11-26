package com.liftoff.project.service.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.mapper.OrderPayUMapper;
import com.liftoff.project.model.OrderPayU;
import com.liftoff.project.repository.OrderPayURepository;
import com.liftoff.project.service.PayUService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayUServiceImpl implements PayUService {

    private final PayUApiClient payUApiClient;
    private final OrderPayURepository orderPayURepository;
    private final OrderPayUMapper orderPayUMapper;

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
    public OrderCreatedResponseDTO addOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO) {


        OrderPayU orderPayU = orderPayUMapper
                .mapOrderResponseDTOTOrderPayU(payUApiClient
               .submitOrder(authorizationHeader, orderCreateRequestDTO));

        orderPayU.setUuid(UUID.randomUUID());

        OrderPayU savedOrderPayU = orderPayURepository
                .save(orderPayU);

        return orderPayUMapper
                .mapOrderToOrderResponseDTO(savedOrderPayU);

    }


}