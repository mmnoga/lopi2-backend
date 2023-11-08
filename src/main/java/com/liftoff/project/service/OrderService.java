package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.request.OrderSummaryForUserRequestDTO;
import com.liftoff.project.controller.order.request.OrderSummaryToSendRequestDTO;
import com.liftoff.project.controller.order.response.DetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryToSendResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.order.Order;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface OrderService {

    /**
     * Edit existed order.
     *
     * @param orderUuid    The UUID of the order to be edited.
     * @param orderRequest The DTO of the order to be edited.
     */
    OrderCreatedResponseDTO editOrder(OrderRequestDTO orderRequest, UUID orderUuid);

    /**
     * Edit existed order`s delivery method.
     *
     * @param uuid                  The UUID of the order to be edited.
     * @param orderChangeRequestDTO The DTO of the order`s deliveryMethodName to be edited.
     */
    OrderCreatedResponseDTO changeOrderDeliveryMethod(OrderDeliveryMethodRequestDTO orderChangeRequestDTO, UUID uuid);

    /**
     * Edit existed order`s payment method.
     *
     * @param uuid                    The UUID of the order to be edited.
     * @param paymentMethodRequestDTO The DTO of the order`s paymentMethodName to be edited.
     */
    OrderCreatedResponseDTO changeOrderPaymentMethod(OrderPaymentMethodRequestDTO paymentMethodRequestDTO, UUID uuid);


    /**
     * Edit existed order`s payment method.
     *
     * @param uuid          The UUID of the order to be edited.
     * @param paymentMethod The String of the order`s paymentMethodName to be edited.
     */
    OrderCreatedResponseDTO changeOrderPaymentMethod(String paymentMethod, UUID uuid);


    /**
     * Add order from OrderRequestDTO.
     *
     * @param orderRequest The DTO of the order to be added.
     * @param cartUuid     The UUID of the cart that its items be added as orderItems .
     */
    OrderCreatedResponseDTO createOrder(OrderRequestDTO orderRequest, UUID cartUuid);


    /**
     * Returns order`s summary as OrderSummaryListResponseDTO.
     */
    OrderSummaryListResponseDTO getAllOrdersSummary();


    /**
     * Returns order`s details as OrderDetailsListResponseDTO.
     */
    OrderDetailsListResponseDTO getAllOrdersDetails();

    /**
     * Returns order`s details by orderUuid as OrderDetailsResponseDTO.
     *
     * @param orderUuid UUID of Order.
     */
    OrderCreatedResponseDTO getOrderByUuid(UUID orderUuid);

    /**
     * Sends an order summary based on the provided request data.
     *
     * @param orderSummaryToSendRequestDTO The request data for sending the order summary.
     * @return An OrderSummaryToSendResponseDTO containing a message indicating
     * that the order summary send has been initialized.
     */
    OrderSummaryToSendResponseDTO sendOrderSummary(OrderSummaryToSendRequestDTO orderSummaryToSendRequestDTO);

    /**
     * Retrieves an Order entity by its universally unique identifier (UUID).
     *
     * @param orderUuid The UUID of the Order to be retrieved.
     * @return The Order entity associated with the provided UUID.
     * @throws BusinessException if the Order with the specified UUID is not found.
     */
    Order getOrderEntityByUuid(UUID orderUuid);

    /**
     * Sends an order summary for an existing user.
     *
     * @param userDetails                   The user details of the existing user.
     * @param orderSummaryForUserRequestDTO The request containing the order UUID.
     * @return An OrderSummaryToSendResponseDTO indicating the status of the order summary sending process.
     */
    OrderSummaryToSendResponseDTO sendOrderSummaryForUser(
            UserDetails userDetails,
            OrderSummaryForUserRequestDTO orderSummaryForUserRequestDTO);

    /**
     * Retrieves order details for a specific order based on its UUID.
     *
     * @param orderUuid The UUID of the order to retrieve details for.
     * @return A DetailsResponseDTO containing the order details.
     */
    DetailsResponseDTO getOrderDetailsByOrderUuid(UUID orderUuid);

    /**
     * Retrieves order details for a specific order based on its UUID,
     * while ensuring the order belongs to the authenticated user.
     *
     * @param userDetails The user details of the authenticated user.
     * @param orderUuid   The UUID of the order to retrieve details for.
     * @return A DetailsResponseDTO containing the order details if the order belongs to the user,
     * or null if the order doesn't belong to the user.
     * @throws BusinessException If the order does not belong to the authenticated user,
     *                           an exception is thrown.
     */
    DetailsResponseDTO getOrderDetailsForUserByOrderUuid(
            UserDetails userDetails,
            UUID orderUuid);

    /**
     * Creates a new order for the authenticated user based on the provided order request.
     *
     * @param userDetails  The user details of the authenticated user.
     * @param orderRequest The order request containing details of the order to be created.
     * @param cartUuid     The UUID of the cart from which the order will be created.
     * @return A OrderCreatedResponseDTO containing information about the newly created order.
     * @throws BusinessException If the terms and conditions are not accepted,
     *                           delivery method or payment method is not found,
     *                           the cart is not found, or there are other business-related issues
     *                           during order creation.
     */
    OrderCreatedResponseDTO createOrderForUser(
            UserDetails userDetails,
            OrderRequestDTO orderRequest,
            UUID cartUuid);

}