package com.liftoff.project.controller.cart.response;

import com.liftoff.project.controller.product.response.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    private ProductResponseDTO product;
    private Integer quantity;

}