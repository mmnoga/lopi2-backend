package com.liftoff.project.controller.order.response;

import com.liftoff.project.controller.response.CartItemResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderSummaryResponseDTO {

    private String customerName;
    private Instant orderDate;
    private List<CartItemResponseDTO> cartItems;
    private double totalPrice;

}
