package com.liftoff.project.controller.order.response;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.model.order.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class OrderDetailsResponseDTO {

    private UUID uId;
    private Instant orderDate;
    private OrderStatus status;
    private Double totalPrice;
    private DeliveryMethodResponseDTO deliveryMethod;
    private AddressResponseDTO shippingAddress;
    private AddressResponseDTO billingAddress;
    private PaymentMethod paymentMethod;
    private CartResponseDTO cart;
    private CustomerResponseDTO customer;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean termsAccepted;

}
