package com.liftoff.project.controller.order.response;

import com.liftoff.project.controller.product.request.ProductItemDataDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DetailsResponseDTO {

    private String orderNumber;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private AddressResponseDTO deliveryAddress;
    private AddressResponseDTO billingAddress;
    private List<ProductItemDataDTO> productList;
    private String paymentMethod;
    private String deliveryMethod;
    private Double deliveryCost;
    private Double totalPrice;

}