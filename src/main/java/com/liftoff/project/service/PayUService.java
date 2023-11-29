package com.liftoff.project.service;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface PayUService {

    /**
     * Retrieves payment methods using the provided authorization header.
     *
     * @return The response containing payment methods.
     * Throws Payu system code if there is an exception during the retrieval process.
     */
    PaymentMethodResponseDTO getPaymentMethods();

    /**
     * Retrieves added OrderPayU as OrderCreatedResponseDTO using the provided authorization header.
     *
     * @param orderCreateRequestDTO The OrderCreateRequestDTO object to persist in database and in PayU service too.
     * @return The response containing added PayU order.
     * Throws Payu system code if there is an exception during the retrieval process.
     */
    OrderCreatedResponseDTO addOrder(OrderCreateRequestDTO orderCreateRequestDTO);

    /**
     * Processes the payment for the specified order.
     *
     * @param orderUuid           The UUID of the order for which the payment is being processed.
     * @return An OrderCreatedResponseDTO representing the response of the payment process.
     */
    OrderCreatedResponseDTO handlePayment(UUID orderUuid, HttpServletRequest request);

    /**
     * Retrieves shop details based on the provided authorization header.
     *
     * @return A ShopDetailsResponseDTO containing details about the shop.
     */
    ShopDetailsResponseDTO getShopDetails();

    /**
     * Retrieves order details based on the provided authorization header and shop ID.
     *
     * @param orderId             The unique identifier of the order.
     * @return                    An instance of OrderDetailsResponseDTO containing
     *                            the detailed information about the specified order.
     */
    OrderDetailsResponseDTO getOrderDetails(String orderId);

}