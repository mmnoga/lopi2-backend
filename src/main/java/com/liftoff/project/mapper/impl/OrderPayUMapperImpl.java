package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.StatusDTO;
import com.liftoff.project.mapper.OrderPayUMapper;
import com.liftoff.project.model.OrderPayU;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPayUMapperImpl implements OrderPayUMapper {

    @Override
    public OrderCreatedResponseDTO mapOrderToOrderResponseDTO(OrderPayU orderPayU) {

        if (orderPayU == null) {
            return null;
        }

        return OrderCreatedResponseDTO.builder()
                .uuid(orderPayU.getUuid())
                .status(StatusDTO.builder().statusCode(orderPayU.getStatusCode()).build())
                .redirectUri(orderPayU.getRedirectUri())
                .orderId(orderPayU.getOrderId())
                .extOrderId(orderPayU.getExtOrderId())
                .build();
    }

    @Override
    public OrderPayU mapOrderResponseDTOTOrderPayU(OrderResponseDTO orderResponseDTO) {

        if (orderResponseDTO == null) {
            return null;
        }

        return OrderPayU.builder()
                .uuid(null)
                .statusCode(orderResponseDTO.getStatus().getStatusCode())
                .redirectUri(orderResponseDTO.getRedirectUri())
                .orderId(orderResponseDTO.getOrderId())
                .extOrderId(orderResponseDTO.getExtOrderId())
                .build();
    }

}