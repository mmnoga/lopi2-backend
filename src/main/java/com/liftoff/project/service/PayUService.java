package com.liftoff.project.service;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface PayUService {

    /**
     * Retrieves the access token from the PayU API using the client credentials.
     *
     * @return A PayUAuthResponseDTO with the access token
     * if the request is successful. If an error occurs, an appropriate HTTP status code and
     * error message will be encapsulated in the response entity.
     */
    PayUAuthResponseDTO getAccessToken();

    /**
     * Retrieves payment methods using the provided authorization header.
     *
     * @param authorizationHeader The authorization header containing the access token.
     * @return The response containing payment methods.
     * Throws Payu system code if there is an exception during the retrieval process.
     */
    PaymentMethodResponseDTO getPaymentMethods(String authorizationHeader);


    /**
     * Retrieves added OrderPayU as OrderCreatedResponseDTO using the provided authorization header.
     *
     * @param authorizationHeader   The authorization header containing the access token.
     * @param orderCreateRequestDTO The OrderCreateRequestDTO object to persist in database and in PayU service too.
     * @return The response containing added PayU order.
     * Throws Payu system code if there is an exception during the retrieval process.
     */
    OrderCreatedResponseDTO addOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO);

    /**
     * Processes the payment for the specified order.
     *
     * @param authorizationHeader The authorization header containing necessary credentials.
     * @param orderUuid           The UUID of the order for which the payment is being processed.
     * @return An OrderCreatedResponseDTO representing the response of the payment process.
     */
    OrderCreatedResponseDTO handlePayment(
            String authorizationHeader,
            UUID orderUuid,
            HttpServletRequest request);

    /**
     * Retrieves shop details based on the provided authorization header.
     *
     * @param authorizationHeader The authorization header used to authenticate the request.
     * @return A ShopDetailsResponseDTO containing details about the shop.
     */
    ShopDetailsResponseDTO getShopDetails(String authorizationHeader);

    /**
     * Retrieves order details based on the provided authorization header and shop ID.
     *
     * @param authorizationHeader The authorization header containing authentication credentials.
     * @param orderId             The unique identifier of the order.
     * @return                    An instance of OrderDetailsResponseDTO containing
     *                            the detailed information about the specified order.
     */
    OrderDetailsResponseDTO getOrderDetails(String authorizationHeader, String orderId);

}