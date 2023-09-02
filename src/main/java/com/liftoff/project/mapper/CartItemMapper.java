package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.CartItemResponseDTO;
import com.liftoff.project.model.CartItem;

public interface CartItemMapper {

    CartItemResponseDTO mapCartItemToCartItemResponseDTO(CartItem cartItem);

}
