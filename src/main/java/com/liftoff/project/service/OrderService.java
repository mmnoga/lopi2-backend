package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    /**
     * Add order from existed cart.
     *
     * @param cartUuid The UUID of the cart that its items be added as orderItems .
     */
    OrderSummaryResponseDTO addOrder(UUID cartUuid);


    /**
     * Edit existed order.
     *
     * @param orderUuid The UUID of the order to be edited.
     * @param orderRequest The DTO of the order to be edited.
     *
     */
    OrderDetailsResponseDTO editOrder(OrderRequestDTO orderRequest, UUID orderUuid);


    OrderDetailsResponseDTO changeOrderDeliveryMethod(OrderDeliveryMethodRequestDTO orderChangeRequestDTO, UUID uuid);
    OrderDetailsResponseDTO changeOrderPaymentMethod(OrderPaymentMethodRequestDTO paymentMethodRequestDTO, UUID uuid);


    OrderDetailsResponseDTO createOrder(OrderRequestDTO orderRequest, UUID cartUuid);

    List<OrderSummaryResponseDTO> getAllOrdersSummary();

    List<OrderDetailsResponseDTO> getAllOrdersDetails();


}