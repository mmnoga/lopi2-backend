package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.Cart;

public interface CartMapper {

    /**
     * Maps the Cart entity to the CartResponseDTO object.
     *
     * @param cart The Cart entity to be mapped.
     * @return The corresponding CartResponseDTO.
     */
    CartResponseDTO mapEntityToResponse(Cart cart);

}
