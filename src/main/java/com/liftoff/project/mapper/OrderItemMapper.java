package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;

public interface OrderItemMapper {


    /**
     * Maps the OrderItemRequestDTO and Order objects to the OrderItem entity.
     *
     * @param orderItemRequestDTO The OrderItemRequestDTO object to be mapped to the OrderItem entity.
     * @param order The Order entity to be mapped to the OrderItem entity
     * @return The mapped OrderItem entity.
     */
    OrderItem mapOrderItemRequestDTOToOrderItem(OrderItemRequestDTO orderItemRequestDTO, Order order);


}
