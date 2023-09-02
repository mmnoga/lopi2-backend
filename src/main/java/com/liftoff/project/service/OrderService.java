package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.OrderChangeRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    /**
     * Add order from existed cart.
     *
     * @param cartUuid The UUID of the cart that its items be added as orderItems .
     */
    void addOrder(UUID cartUuid);


    /**
     * Edit existed order.
     *
     * @param orderUuid The UUID of the order to be edited.
     * @param orderRequest The DTO of the order to be edited.
     *
     */
    void editOrder(OrderRequestDTO orderRequest, UUID orderUuid);


    void changeDeliveryMethod(OrderChangeRequestDTO orderChangeRequestDTO);
    void changePaymentMethod(OrderChangeRequestDTO orderChangeRequestDTO);


    OrderDetailsResponseDTO createOrder(OrderRequestDTO orderRequest, UUID cartUuid);

    List<OrderSummaryResponseDTO> getAllOrdersSummary();

    List<OrderDetailsResponseDTO> getAllOrdersDetails();


}