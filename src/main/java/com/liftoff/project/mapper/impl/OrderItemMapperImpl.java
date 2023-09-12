package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.mapper.OrderItemMapper;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapperImpl implements OrderItemMapper {


    private ProductMapper productMapper;

    @Override
    public OrderItem mapOrderItemRequestDTOToOrderItem(OrderItemRequestDTO orderItemRequestDTO, Order order) {

        if (order == null || orderItemRequestDTO == null) {
            return null;
        }

        OrderItem orderItem = OrderItem.builder()
                .withOrder(order)
                .withProduct(productMapper.mapRequestToEntity(orderItemRequestDTO.getProductRequestDTO()))
                .withQuantity(orderItemRequestDTO.getQuantity())
                .withSubtotal(orderItemRequestDTO.getSubtotal())
                .withUnitPrice(orderItemRequestDTO.getUnitPrice())
                .build();

        return orderItem;
    }


}