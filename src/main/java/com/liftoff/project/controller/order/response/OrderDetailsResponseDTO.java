package com.liftoff.project.controller.order.response;

import com.liftoff.project.controller.product.response.ProductNameResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDetailsResponseDTO {

    private UUID orderUid;
    private String deliveryMethod;
    private String customerEmail;
    private AddressResponseDTO deliveryAddress;
    private String paymentMethod;
    private Instant orderDate;
    private String customerPhone;
    private List<ProductNameResponseDTO> productNameResponseDTOS;
    private Double totalPrice;
    private Double deliveryCost;
}
