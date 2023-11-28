package com.liftoff.project.api;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;

public interface PayUApiClient {

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
     * Retrieves answer for request the creation new Order in PayU system.
     *
     * @param authorizationHeader   The authorization header containing the access token.
     * @param orderCreateRequestDTO The request DTO object with data for new Order which is sending to PayU.
     * @return The response contains the state of new Order in DTO object.
     */
    OrderResponseDTO submitOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO);

    /**
     * Retrieves shop details based on the provided authorization header and shop ID.
     *
     * @param authorizationHeader The authorization header used to authenticate the request.
     * @param shopId              The ID of the shop for which details are requested.
     * @return A ShopDetailsResponseDTO containing details about the specified shop.
     */
    ShopDetailsResponseDTO getShopDetails(
            String authorizationHeader,
            String shopId);

    /**
     * Retrieves order details based on the provided authorization header and shop ID.
     *
     * @param authorizationHeader The authorization header containing authentication credentials.
     * @param shopId              The unique identifier of the shop associated with the order.
     * @return                    An instance of OrderDetailsResponseDTO containing
     *                            the detailed information about the specified order.
     */
    OrderDetailsResponseDTO getOrderDetails(String authorizationHeader, String shopId);

}