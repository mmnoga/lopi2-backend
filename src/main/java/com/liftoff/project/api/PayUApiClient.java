package com.liftoff.project.api;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;

public interface PayUApiClient {

    /**
     * Retrieves payment methods using the provided authorization header.
     *
     * @return The response containing payment methods.
     * Throws Payu system code if there is an exception during the retrieval process.
     */
    PaymentMethodResponseDTO getPaymentMethods();

    /**
     * Retrieves answer for request the creation new Order in PayU system.
     *
     * @param orderCreateRequestDTO The request DTO object with data for new Order which is sending to PayU.
     * @return The response contains the state of new Order in DTO object.
     */
    OrderResponseDTO submitOrder(OrderCreateRequestDTO orderCreateRequestDTO);

    /**
     * Retrieves shop details based on the provided authorization header and shop ID.
     *
     * @param shopId The ID of the shop for which details are requested.
     * @return A ShopDetailsResponseDTO containing details about the specified shop.
     */
    ShopDetailsResponseDTO getShopDetails(String shopId);

    /**
     * Retrieves order details based on the provided authorization header and shop ID.
     *
     * @param shopId The unique identifier of the shop associated with the order.
     * @return An instance of OrderDetailsResponseDTO containing
     * the detailed information about the specified order.
     */
    OrderDetailsResponseDTO getOrderDetails(String shopId);

}