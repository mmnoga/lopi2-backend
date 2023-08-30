package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.CartItemResponseDTO;
import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;

public interface CartMapper {

    /**
     * Maps the Cart entity to the CartResponseDTO object.
     *
     * @param cart The Cart entity to be mapped.
     * @return The corresponding CartResponseDTO.
     */
    CartResponseDTO mapEntityToResponse(Cart cart);

    /**
     * Maps a {@link CartItem} entity to a {@link CartItemResponseDTO}.
     *
     * @param cartItem The {@link CartItem} entity to be mapped.
     * @return The corresponding {@link CartItemResponseDTO} containing mapped data.
     */
    CartItemResponseDTO mapCartItemToCartItemResponseDTO(CartItem cartItem);

}
