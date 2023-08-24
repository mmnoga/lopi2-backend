package com.liftoff.project.service.impl;

import com.liftoff.project.exception.CartNotFoundException;
import com.liftoff.project.model.Cart;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final String CART_ID_COOKIE_NAME = "cartId";

    private final CartRepository cartRepository;
    private final CookieService cookieService;

    @Override
    public Optional<Cart> getCartByUuid(UUID cartUuid) {
        return cartRepository.findByUuid(cartUuid);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart createNewCart(HttpServletResponse response) {
        Cart cart = new Cart();
        cart.setUuid(UUID.randomUUID());
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        Cart savedCart = saveCart(cart);

        cookieService.setCookie(
                CART_ID_COOKIE_NAME,
                savedCart.getUuid().toString(),
                response);

        return savedCart;
    }

    @Override
    public Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response) {
        UUID cartUuid = extractCartUuidFromCookies(request.getCookies());

        Cart cart;

        if (response != null) {
            cart = cartRepository.findByUuid(cartUuid)
                    .orElseGet(() -> createNewCart(response));
        } else {
            cart = cartRepository.findByUuid(cartUuid)
                    .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        }

        return cart;
    }

    private UUID extractCartUuidFromCookies(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> CART_ID_COOKIE_NAME.equals(cookie.getName()))
                .map(cookie -> UUID.fromString(cookie.getValue()))
                .findFirst()
                .orElse(null);
    }

}
