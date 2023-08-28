package com.liftoff.project.service.impl;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.CartNotFoundException;
import com.liftoff.project.exception.CookieNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.exception.ProductOutOfStockException;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.CookieService;
import com.liftoff.project.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Value("${cart.cookie.name}")
    private String CART_ID_COOKIE_NAME;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CookieService cookieService;
    private final ProductService productService;
    private final CartMapper cartMapper;

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(HttpServletRequest request) {
        String cartId = cookieService
                .getCookieValue(CART_ID_COOKIE_NAME, request);

        cartRepository.findByUuid(UUID.fromString(cartId))
                .ifPresent(cart -> {
                    cart.getProducts().clear();
                    cart.setTotalPrice(0.0);
                    cart.setTotalQuantity(0);
                    saveCart(cart);
                });
    }

    @Override
    public String addToCart(String cartId, UUID productUid) {
        Cart cart = getOrCreateCartByCartId(cartId);

        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }

        Product productToAdd = productService.getProductEntityByUuid(productUid);

        if (productToAdd == null) {
            throw new ProductNotFoundException("Product not found");
        }

        if (productToAdd.getQuantity() <= 0) {
            throw new ProductOutOfStockException("Product is out of stock");
        }

        cart.getProducts().add(productToAdd);
        productToAdd.setQuantity(productToAdd.getQuantity() - 1);

        updateCartTotals(cart);
        cartRepository.save(cart);

        return "Product " + productUid + " added to cart " + cart.getUuid();
    }

    @Override
    @Transactional
    public void mergeCartWithAuthenticatedUser(String unauthenticatedCartId, String authenticatedCartId) {
        Cart unauthenticatedCart = cartRepository
                .findByUuid(UUID.fromString(unauthenticatedCartId))
                .orElseThrow(() -> new CartNotFoundException("Unauthenticated cart not found"));

        Cart authenticatedCart = cartRepository
                .findByUuid(UUID.fromString(authenticatedCartId))
                .orElseThrow(() -> new CartNotFoundException("Authenticated cart not found"));

        List<Product> unauthenticatedProducts = unauthenticatedCart.getProducts();
        List<Product> authenticatedProducts = authenticatedCart.getProducts();

        authenticatedProducts.addAll(unauthenticatedProducts);

        double totalUnauthenticatedPrice = unauthenticatedCart.getTotalPrice();
        int totalUnauthenticatedQuantity = unauthenticatedCart.getTotalQuantity();

        authenticatedCart.setTotalPrice(
                authenticatedCart.getTotalPrice() + totalUnauthenticatedPrice);
        authenticatedCart.setTotalQuantity(
                authenticatedCart.getTotalQuantity() + totalUnauthenticatedQuantity);

        unauthenticatedProducts.clear();
        unauthenticatedCart.setTotalPrice(0.0);
        unauthenticatedCart.setTotalQuantity(0);

        cartRepository.save(authenticatedCart);
        cartRepository.save(unauthenticatedCart);
    }

    @Override
    public void processCart(UUID productUuid, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = getOrCreateCart(request, response);
        Product product = productService
                .getProductEntityByUuid(productUuid);

        List<Product> productList = new ArrayList<>(cart.getProducts());
        productList.add(product);

        cart.setProducts(productList);
        cart.setTotalPrice(cart.getTotalPrice() + product.getRegularPrice());
        cart.setTotalQuantity(cart.getTotalQuantity() + 1);

        saveCart(cart);
    }

    @Override
    public Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response) {
        try {
            String cartId = cookieService
                    .getCookieValue(CART_ID_COOKIE_NAME, request);
            return cartRepository
                    .findByUuid(UUID.fromString(cartId))
                    .orElseGet(() -> createEmptyCart());
        } catch (CookieNotFoundException ex) {
            return createNewCartOrThrow(response, CART_ID_COOKIE_NAME);
        }
    }

    @Override
    public CartResponseDTO getCart(String cartId) {
        Cart cart = getOrCreateCartByCartId(cartId);

        return cartMapper.mapEntityToResponse(cart);
    }

    private void updateCartTotals(Cart cart) {
        double totalPrice = cart.getProducts().stream()
                .filter(product -> product.getId() != null)
                .mapToDouble(Product::getRegularPrice)
                .sum();

        int totalQuantity = (int) cart.getProducts().stream()
                .filter(product -> product.getId() != null)
                .count();

        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);

        cartRepository.save(cart);
    }

    private Cart createEmptyCart() {
        Cart cart = new Cart();
        cart.setUuid(UUID.randomUUID());
        cart.setProducts(Collections.emptyList());
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        return saveCart(cart);
    }

    private Cart getOrCreateCartByCartId(String cartId) {
        UUID cartUuid = UUID.fromString(cartId);

        return cartRepository.findByUuid(cartUuid)
                .orElseGet(() -> createCartWithUuid(cartUuid));
    }

    private Cart createCartWithUuid(UUID cartUuid) {
        Cart cart = new Cart();

        cart.setUuid(cartUuid);
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        return saveCart(cart);
    }

    private Cart createNewCartOrThrow(HttpServletResponse response, String cookieName) {
        if (response != null) {
            Cart newCart = createEmptyCart();
            cookieService
                    .setCookie(CART_ID_COOKIE_NAME, newCart.getUuid().toString(), response);
            return newCart;
        } else {
            throw new CookieNotFoundException("Cookie with name " + cookieName + " not found");
        }
    }

}
