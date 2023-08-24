package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.Cart;

public class CartMapper implements com.liftoff.project.mapper.CartMapper {
    @Override
    public CartResponseDTO mapCartToCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();

        cartResponseDTO.set(cart.getUuid());
        cartResponseDTO.setUser(cart.getUser());  // Assuming you have a similar conversion method for User
        cartResponseDTO.setProducts(cart.getProducts());
        cartResponseDTO.setTotalPrice(cart.getTotalPrice());
        cartResponseDTO.setTotalQuantity(cart.getTotalQuantity());

        // You can set any other properties from Cart to CartResponseDTO as needed

        return cartResponseDTO; null;
    }
}
