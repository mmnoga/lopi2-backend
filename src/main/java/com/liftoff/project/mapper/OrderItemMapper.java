package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;

public interface OrderItemMapper {

    OrderItem mapOrderItemRequestDTOToOrderItem(OrderItemRequestDTO orderItemRequestDTO, Order order);


}
