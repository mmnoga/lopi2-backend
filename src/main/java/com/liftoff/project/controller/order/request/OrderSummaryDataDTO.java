package com.liftoff.project.controller.order.request;

import com.liftoff.project.controller.product.request.ProductItemDataDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class OrderSummaryDataDTO {

    String orderNumber;
    List<ProductItemDataDTO> productList;
    Double totalProductsPrice;
    String deliveryMethod;
    String paymentMethod;
    Double totalPrice;
    String customerName;
    String billingStreetAndNumber;
    String billingPostalCodeAndCity;
    String phoneNumber;
    String customerEmail;
    String deliveryStreetAndNumber;
    String deliveryPostalCodeAndCity;
    String email;

}