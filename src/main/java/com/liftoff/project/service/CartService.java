package com.liftoff.project.service;

import com.liftoff.project.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.UUID;

public interface CartService {

    Cart createNewCart(HttpServletResponse response);

    Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response);

    Optional<Cart> getCartByUuid(UUID cartUuid);

    Cart saveCart(Cart cart);

}
