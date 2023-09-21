package com.liftoff.project.mapper;

import com.liftoff.project.controller.cart.response.CartItemResponseDTO;
import com.liftoff.project.model.CartItem;

public interface CartItemMapper {

    CartItemResponseDTO mapCartItemToCartItemResponseDTO(CartItem cartItem);

}
