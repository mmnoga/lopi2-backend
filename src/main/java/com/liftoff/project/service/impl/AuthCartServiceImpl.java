package com.liftoff.project.service.impl;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.CartNotFoundException;
import com.liftoff.project.model.Cart;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.AuthCartService;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthCartServiceImpl implements AuthCartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Override
    public String findCartIdByUsername(String username) {
        return cartRepository.findByUserUsername(username)
                .map(Cart::getUuid)
                .map(UUID::toString)
                .orElse(null);
    }

    @Override
    public String createCartForUser(String username) {
        AtomicReference<String> cartIdRef = new AtomicReference<>(null);

        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    String cartId = UUID.randomUUID().toString();
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setUuid(UUID.fromString(cartId));
                    cart.setTotalPrice(0.0);
                    cart.setTotalQuantity(0);
                    cartRepository.save(cart);
                    cartIdRef.set(cartId);
                });

        return cartIdRef.get();
    }

    @Override
    public String processCartForUser(String username, UUID productUid) {
        String cartId = findCartIdByUsername(username);

        if (cartId == null) {
            cartId = createCartForUser(username);
        }

        return cartService.addToCart(cartId, productUid);
    }

    @Override
    public CartResponseDTO viewCartForUser(String username) {
        String cartId = findCartIdByUsername(username);

        if (cartId == null) {
            return new CartResponseDTO();
        }

        return cartService.getCart(cartId);
    }

    @Override
    public void clearCartForUser(String username) {
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found"));

        cart.getProducts().clear();
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        cartService.saveCart(cart);
    }

}
