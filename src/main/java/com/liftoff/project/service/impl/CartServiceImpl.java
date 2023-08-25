package com.liftoff.project.service.impl;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.CartNotFoundException;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final String CART_ID_COOKIE_NAME = "cartId";

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
        Cart cart = getOrCreateCart(request, null);

        cart.getProducts().clear();
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        saveCart(cart);
    }

    @Override
    public void clearUserCart(String cartId) {
        Cart cart = getOrCreateCartByCartId(cartId);

        cart.getProducts().clear();
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        saveCart(cart);
    }

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
    public String addToCart(String cartId, UUID productUid) {
        Cart cart = getOrCreateCartByCartId(cartId);

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
    public void mergeCartWithAuthenticatedUser(
            String unauthenticatedCartId, String authenticatedCartId) {
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
                    .orElseThrow(() -> new CartNotFoundException("Shopping cart not found"));
        }

        return cart;
    }

    @Override
    public CartResponseDTO getCart(String cartId) {
        Cart cart = getOrCreateCartByCartId(cartId);

        return cartMapper.mapEntityToResponse(cart);
    }

    private UUID extractCartUuidFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> CART_ID_COOKIE_NAME.equals(cookie.getName()))
                    .map(cookie -> UUID.fromString(cookie.getValue()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
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

    private Cart createNewCartWithUuid(UUID cartUuid) {
        Cart cart = new Cart();
        cart.setUuid(cartUuid);
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        return saveCart(cart);
    }

    private Cart getOrCreateCartByCartId(String cartId) {
        UUID cartUuid = UUID.fromString(cartId);

        return cartRepository.findByUuid(cartUuid)
                .orElseGet(() -> createNewCartWithUuid(cartUuid));
    }

}
