package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;

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
     * @param orderUuid    The UUID of the order to be edited.
     * @param orderRequest The DTO of the order to be edited.
     */
    OrderDetailsResponseDTO editOrder(OrderRequestDTO orderRequest, UUID orderUuid);

    /**
     * Edit existed order`s delivery method.
     *
     * @param uuid                  The UUID of the order to be edited.
     * @param orderChangeRequestDTO The DTO of the order`s deliveryMethodName to be edited.
     */
    OrderDetailsResponseDTO changeOrderDeliveryMethod(OrderDeliveryMethodRequestDTO orderChangeRequestDTO, UUID uuid);

    /**
     * Edit existed order`s payment method.
     *
     * @param uuid                    The UUID of the order to be edited.
     * @param paymentMethodRequestDTO The DTO of the order`s paymentMethodName to be edited.
     */
    OrderDetailsResponseDTO changeOrderPaymentMethod(OrderPaymentMethodRequestDTO paymentMethodRequestDTO, UUID uuid);


    /**
     * Edit existed order`s payment method.
     *
     * @param uuid                    The UUID of the order to be edited.
     * @param paymentMethod The String of the order`s paymentMethodName to be edited.
     */
    OrderDetailsResponseDTO changeOrderPaymentMethod(String paymentMethod, UUID uuid);


    /**
     * Add order from OrderRequestDTO.
     *
     * @param orderRequest The DTO of the order to be added.
     * @param cartUuid     The UUID of the cart that its items be added as orderItems .
     */
    OrderDetailsResponseDTO createOrder(OrderRequestDTO orderRequest, UUID cartUuid);


    /**
     * Returns order`s summary as OrderSummaryListResponseDTO.
     */
    OrderSummaryListResponseDTO getAllOrdersSummary();


    /**
     * Returns order`s details as OrderDetailsListResponseDTO.
     */
    OrderDetailsListResponseDTO getAllOrdersDetails();


}