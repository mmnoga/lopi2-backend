package com.liftoff.project.service.impl;

import com.liftoff.project.exception.cart.CartNotFoundException;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.Session;
import com.liftoff.project.repository.CartItemRepository;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.CookieService;
import com.liftoff.project.service.ProductService;
import com.liftoff.project.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Value("${cart.cookie.name}")
    private String cookieName;
    @Value("${cart.cookie.maxAgeSeconds}")
    private Long cookieMaxTime;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CookieService cookieService;
    private final ProductService productService;
    private final SessionService sessionService;

    @Override
    public void clearCart(HttpServletRequest request) {
        String cartId = cookieService
                .getCookieValue(cookieName, request);

        cartRepository.findByUuid(UUID.fromString(cartId))
                .ifPresent(cart -> {
                    List<CartItem> cartItems = cart.getCartItems();

                    for (CartItem cartItem : cartItems) {
                        cartItem.setCart(null);
                    }

                    cartItems.clear();
                    cart.setTotalPrice(0.0);
                    cart.setTotalQuantity(0);
                    cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCart(String cartId) {
        return cartRepository.findByUuid(UUID.fromString(cartId))
                .orElseThrow(() -> new CartNotFoundException("Cart not found with UUID: " + cartId));
    }

    @Override
    @Transactional
    public void processCart(UUID productUuid, int quantity,
                            HttpServletRequest request, HttpServletResponse response) {
        Cart cart = getCartByCookieOrCreateNewCart(request, response);

        Product product = productService.getProductEntityByUuid(productUuid);

        boolean productAlreadyInCart = false;
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProduct().equals(product)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                productAlreadyInCart = true;
                break;
            }
        }

        if (!productAlreadyInCart) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);

            cartItemRepository.save(cartItem);
        }

        cart.setTotalPrice(cart.getTotalPrice() + (product.getRegularPrice() * quantity));
        cart.setTotalQuantity(cart.getTotalQuantity() + quantity);

        cartRepository.save(cart);
    }

    @Override
    public void mergeCartWithAuthenticatedUser(String unauthenticatedCartId, String authenticatedCartId) {
        Cart unauthenticatedCart = cartRepository
                .findByUuid(UUID.fromString(unauthenticatedCartId))
                .orElseThrow(() -> new CartNotFoundException("Unauthenticated cart not found"));

        Cart authenticatedCart = cartRepository
                .findByUuid(UUID.fromString(authenticatedCartId))
                .orElseThrow(() -> new CartNotFoundException("Authenticated cart not found"));

        List<CartItem> unauthenticatedCartItems = unauthenticatedCart.getCartItems();
        List<CartItem> authenticatedCartItems = authenticatedCart.getCartItems();

        for (CartItem cartItem : unauthenticatedCartItems) {
            cartItem.setCart(authenticatedCart);
        }

        authenticatedCartItems.addAll(unauthenticatedCartItems);

        double totalUnauthenticatedPrice = unauthenticatedCart.getTotalPrice();
        int totalUnauthenticatedQuantity = unauthenticatedCart.getTotalQuantity();

        authenticatedCart.setTotalPrice(
                authenticatedCart.getTotalPrice() + totalUnauthenticatedPrice);
        authenticatedCart.setTotalQuantity(
                authenticatedCart.getTotalQuantity() + totalUnauthenticatedQuantity);

        unauthenticatedCartItems.clear();
        unauthenticatedCart.setTotalPrice(0.0);
        unauthenticatedCart.setTotalQuantity(0);

        cartRepository.save(authenticatedCart);
        cartRepository.save(unauthenticatedCart);
    }

    @Override
    public Cart getCartByCookieOrCreateNewCart(HttpServletRequest request, HttpServletResponse response) {
        String cartId = cookieService.getCookieValue(cookieName, request);

        if (cartId != null) {
            return cartRepository.findByUuid(UUID.fromString(cartId))
                    .orElseGet(() -> createCartWithId(cartId));
        } else if (response != null) {
            Cart newCart = createCart();
            String newCartId = newCart.getUuid().toString();
            cookieService.setCookie(cookieName, newCartId, response);

            Instant expirationTime = Instant.now()
                    .plusSeconds(cookieMaxTime);
            Session session = sessionService
                    .createSession(newCart.getUuid(), expirationTime);
            newCart.setSession(session);

            return cartRepository.save(newCart);
        } else {
            return null;
        }
    }

    private Cart createCart() {
        Cart cart = new Cart();

        cart.setUuid(UUID.randomUUID());
        cart.setCartItems(Collections.emptyList());
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        return cartRepository.save(cart);
    }

    private Cart createCartWithId(String cartId) {
        Cart cart = new Cart();

        cart.setUuid(UUID.fromString(cartId));
        cart.setCartItems(Collections.emptyList());
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        return cartRepository.save(cart);
    }

}