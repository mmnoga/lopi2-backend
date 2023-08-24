package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMapperImpl implements CartMapper {

    private final ProductMapper productMapper;

    @Override
    public CartResponseDTO mapEntityToResponse(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setProducts(cart.getProducts().stream()
                .map(productMapper::mapEntityToResponse)
                .collect(Collectors.toList()));
        cartResponseDTO.setTotalPrice(cart.getTotalPrice());
        cartResponseDTO.setTotalQuantity(cart.getTotalQuantity());
        cartResponseDTO.setCreatedAt(cart.getCreatedAt());
        cartResponseDTO.setUpdatedAt(cart.getUpdatedAt());

        return cartResponseDTO;
    }

}
