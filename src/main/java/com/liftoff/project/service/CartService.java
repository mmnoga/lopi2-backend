package com.liftoff.project.service;

import com.liftoff.project.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CartService {

    Cart createNewCart(HttpServletResponse response);

    Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response);

    Cart saveCart(Cart cart);

    void clearCart(HttpServletRequest request);

}
