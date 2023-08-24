package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.Cart;

public interface CartMapper {

    CartResponseDTO mapEntityToResponse(Cart cart);

}
