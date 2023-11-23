package com.liftoff.project.service;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;

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

    OrderResponseDTO submitOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO);

}