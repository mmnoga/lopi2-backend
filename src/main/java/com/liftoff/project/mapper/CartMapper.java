package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.CartItemResponseDTO;
import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;

public interface CartMapper {

    /**
     * Maps an object of the Cart class to an object of the CartResponseDTO.
     *
     * @param cart The Cart object to be mapped to a CartResponseDTO object.
     * @return A CartResponseDTO object containing the mapped information from the Cart object.
     */
    CartResponseDTO mapCartToCartResponseDTO(Cart cart);

    /**
     * Maps a {@link CartItem} entity to a {@link CartItemResponseDTO}.
     *
     * @param cartItem The {@link CartItem} entity to be mapped.
     * @return The corresponding {@link CartItemResponseDTO} containing mapped data.
     */
    CartItemResponseDTO mapCartItemToCartItemResponseDTO(CartItem cartItem);

}
