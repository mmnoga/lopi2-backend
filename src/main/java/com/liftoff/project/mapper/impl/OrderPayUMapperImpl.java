package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.payu.response.OrderPayUCreatedResponseDTO;
import com.liftoff.project.mapper.OrderPayUMapper;
import com.liftoff.project.model.OrderPayU;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPayUMapperImpl implements OrderPayUMapper {


    @Override
    public OrderPayUCreatedResponseDTO mapOrderPayUToOrderPayUCreatedResponseDTO(OrderPayU orderPayU) {

        if (orderPayU == null) {
            return null;
        }

        return OrderPayUCreatedResponseDTO.builder()
                .uuid(orderPayU.getUuid())
                .statusCode(orderPayU.getStatusCode())
                .redirectUri(orderPayU.getRedirectUri())
                .orderId(orderPayU.getOrderId())
                .extOrderId(orderPayU.getExtOrderId())
                .build();
    }


}