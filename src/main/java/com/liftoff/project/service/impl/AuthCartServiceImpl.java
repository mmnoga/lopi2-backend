package com.liftoff.project.service.impl;

import com.liftoff.project.controller.response.CartItemResponseDTO;
import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.cart.CartNotFoundException;
import com.liftoff.project.exception.product.ProductNotFoundException;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.Product;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.AuthCartService;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthCartServiceImpl implements AuthCartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final CartMapper cartMapper;

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
    @Transactional
    public String processCartForUser(String username, UUID productUid, int quantity) {
        String cartId = findCartIdByUsername(username);

        if (cartId == null) {
            cartId = createCartForUser(username);
        }

        try {
            Cart cart = cartRepository
                    .findByUuid(UUID.fromString(cartId))
                    .orElseThrow(() -> new CartNotFoundException("Cart not found"));

            Product product = productService.getProductEntityByUuid(productUid);

            if (product != null) {
                addProductToCart(cart, product, quantity);
                return "Product " + cartId + " added to cart";
            } else {
                throw new ProductNotFoundException("Product not found");
            }
        } catch (CartNotFoundException ex) {
            throw new CartNotFoundException("Cart not found");
        }

    }

    @Override
    public CartResponseDTO viewCartForUser(String username) {
        String cartId = findCartIdByUsername(username);

        if (cartId == null) {
            return new CartResponseDTO();
        }

        Cart cart = cartService.getCart(cartId);

        if (cart == null) {
            throw new CartNotFoundException("Cart not found for user: " + username);
        }

        CartResponseDTO cartResponse = new CartResponseDTO();
        cartResponse.setUuid(cart.getUuid());

        List<CartItemResponseDTO> cartItemResponseList = cart.getCartItems().stream()
                .map(cartMapper::mapCartItemToCartItemResponseDTO)
                .collect(Collectors.toList());

        cartResponse.setCartItems(cartItemResponseList);
        cartResponse.setTotalPrice(cart.getTotalPrice());
        cartResponse.setTotalQuantity(cart.getTotalQuantity());
        cartResponse.setCreatedAt(cart.getCreatedAt());
        cartResponse.setUpdatedAt(cart.getUpdatedAt());

        return cartResponse;
    }

    @Override
    public void clearCartForUser(String username) {
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found"));

        List<CartItem> cartItems = cart.getCartItems();

        for (CartItem cartItem : cartItems) {
            cartItem.setCart(null);
        }

        cartItems.clear();
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        cartRepository.save(cart);
    }

    private void addProductToCart(Cart cart, Product product, int quantity) {
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
        }

        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getRegularPrice() * item.getQuantity())
                .sum();
        int totalQuantity = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);

        cartRepository.save(cart);
    }

}
