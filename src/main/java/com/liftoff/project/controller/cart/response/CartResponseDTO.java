package com.liftoff.project.controller.cart.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private UUID uuid;
    private List<CartItemResponseDTO> cartItems;
    private Double totalPrice;
    private Integer totalQuantity;

}
