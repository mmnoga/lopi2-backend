package com.liftoff.project.service;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface CartService {

    Cart createNewCart(HttpServletResponse response);

    Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response);

    CartResponseDTO getCart(String cartId);

    Cart saveCart(Cart cart);

    void clearCart(HttpServletRequest request);

    void clearUserCart(String cartId);

    String findCartIdByUsername(String username);

    void createCartForUser(String username, String cartId);

    String addToCart(String cartId, UUID productUid);

}
