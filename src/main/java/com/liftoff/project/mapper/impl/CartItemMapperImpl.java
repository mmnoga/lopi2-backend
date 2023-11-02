package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.cart.response.CartItemResponseDTO;
import com.liftoff.project.mapper.CartItemMapper;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemMapperImpl implements CartItemMapper {

    private final ProductMapper productMapper;

    @Override
    public CartItemResponseDTO mapCartItemToCartItemResponseDTO(CartItem cartItem) {
        return CartItemResponseDTO.builder()
                .product(productMapper.mapEntityToResponse(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .build();
    }
}

