package com.liftoff.project.service.impl;

import com.liftoff.project.controller.cart.response.CartResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.CartMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final CartMapper cartMapper;

    @Override
    public void clearCart(HttpServletRequest request) {
        String cartId = cookieService
                .getCookieValue(cookieName, request);

        Cart cart = cartRepository.findByUuid(UUID.fromString(cartId))
                .orElseThrow(() -> new BusinessException("Cart with UUID: " + cartId + " not found"));

        clearCart(cart);
    }

    @Override
    public Cart getCart(String cartId) {
        return cartRepository.findByUuid(UUID.fromString(cartId))
                .orElseThrow(() -> new BusinessException("Cart with UUID: " + cartId + " not found"));
    }

    @Override
    @Transactional
    public Cart processCart(UUID productUuid, int quantity,
                            HttpServletRequest request, HttpServletResponse response) {
        Cart cart = getCartByCookieOrCreateNewCart(request, response);

        Product product = productService.getProductEntityByUuid(productUuid);

        if (!hasProductEnoughQuantity(product, quantity, cart)) {
            throw new BusinessException("Not enough quantity of product with UUID: "
                    + product.getUId(), HttpStatus.BAD_REQUEST);
        }

        return addProductToCart(cart, product, quantity);
    }

    @Override
    public void mergeCartWithAuthenticatedUser(String unauthenticatedCartId, String authenticatedCartId) {
        if (unauthenticatedCartId != null) {
            Cart unauthenticatedCart = cartRepository
                    .findByUuid(UUID.fromString(unauthenticatedCartId))
                    .orElseThrow(() -> new BusinessException("Unauthenticated cart not found"));

            Cart authenticatedCart = cartRepository
                    .findByUuid(UUID.fromString(authenticatedCartId))
                    .orElseThrow(() -> new BusinessException("Authenticated cart not found"));

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
            throw new BusinessException("Cart not found or cannot be created");
        }
    }

    @Override
    public Cart addProductToCart(Cart cart, Product product, int quantity) {
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

            cart.addCartItem(cartItem);
        }

        if (isDiscountPriceValid(product)) {
            cart.setTotalPrice(cart.getTotalPrice() + (product.getDiscountPrice() * quantity));
        } else {
            cart.setTotalPrice(cart.getTotalPrice() + (product.getRegularPrice() * quantity));
        }

        cart.setTotalQuantity(cart.getTotalQuantity() + quantity);

        return cartRepository.save(cart);
    }

    @Override
    public boolean hasProductEnoughQuantity(Product product, int quantity, Cart cart) {
        List<CartItem> existingItems = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .toList();

        int totalExistingQuantity = existingItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        int availableQuantity = product.getQuantity();

        int totalQuantityAfterAddition = totalExistingQuantity + quantity;

        return totalQuantityAfterAddition <= availableQuantity;
    }

    @Override
    public void removeProduct(UUID productUuid, HttpServletRequest request) {
        String cartId = cookieService.getCookieValue(cookieName, request);

        Cart cart = cartRepository.findByUuid(UUID.fromString(cartId))
                .orElseThrow(() -> new BusinessException("Cart with UUID: " + cartId + " not found"));

        CartItem itemToRemove = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getUId().equals(productUuid))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found in the cart"));

        Double productPrice = itemToRemove.getProduct().getRegularPrice();
        Double productDiscountPrice = itemToRemove.getProduct().getDiscountPrice();
        Integer productQuantity = itemToRemove.getQuantity();

        Double productTotalPrice = (productDiscountPrice != null
                && isDiscountPriceValid(itemToRemove.getProduct()))
                ? productDiscountPrice * productQuantity
                : productPrice * productQuantity;

        cart.setTotalPrice(cart.getTotalPrice() - productTotalPrice);
        cart.setTotalQuantity(cart.getTotalQuantity() - productQuantity);

        cart.getCartItems().remove(itemToRemove);
        itemToRemove.setCart(null);

        cartRepository.save(cart);
        cartItemRepository.delete(itemToRemove);
    }

    @Override
    public CartResponseDTO updateCart(
            UUID productUuid, int quantity,
            HttpServletRequest request, HttpServletResponse response) {

        Cart cart = getCartByCookieOrCreateNewCart(request, response);

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero", HttpStatus.BAD_REQUEST);
        }

        Cart updatedCart = createCartCopy(cart);

        CartItem cartItem = updatedCart.getCartItems().stream()
                .filter(item -> item.getProduct().getUId().equals(productUuid))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Product not found in the cart"));

        if (!hasProductEnoughQuantityForEdit(cartItem.getProduct(), quantity)) {
            throw new BusinessException(
                    "Insufficient quantity of product with UUID: " + productUuid + " in stock", HttpStatus.BAD_REQUEST);
        }

        cartItem.setQuantity(quantity);

        Cart finalCart = calculateTotalPriceAndTotalQuantity(updatedCart);

        finalCart.setUpdatedAt(Instant.now());

        Cart savedCart = cartRepository.save(finalCart);

        return cartMapper.mapCartToCartResponseDTO(savedCart);
    }

    @Override
    public boolean hasProductEnoughQuantityForEdit(Product product, int quantity) {
        int availableQuantity = product.getQuantity();

        return quantity >= 0 && quantity <= availableQuantity;
    }

    @Override
    public Cart calculateTotalPriceAndTotalQuantity(Cart cart) {
        double totalPrice = 0.0;
        int totalQuantity = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            Integer productQuantity = cartItem.getQuantity();

            if (isDiscountPriceValid(product)) {
                totalPrice += (product.getDiscountPrice() * productQuantity);
            } else {
                totalPrice += (product.getRegularPrice() * productQuantity);
            }

            totalQuantity += productQuantity;
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);

        return cartRepository.save(cart);
    }

    private Cart createCartCopy(Cart cart) {
        Cart copyCart = new Cart();
        copyCart.setId(cart.getId());
        copyCart.setUuid(cart.getUuid());
        copyCart.setUser(cart.getUser());
        copyCart.setCartItems(new ArrayList<>(cart.getCartItems()));
        copyCart.setTotalPrice(cart.getTotalPrice());
        copyCart.setTotalQuantity(cart.getTotalQuantity());
        copyCart.setCreatedAt(cart.getCreatedAt());
        copyCart.setUpdatedAt(cart.getUpdatedAt());
        copyCart.setSession(cart.getSession());

        return copyCart;
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.setUuid(UUID.randomUUID());

        return cartRepository.save(cart);
    }

    private Cart createCartWithId(String cartId) {
        Cart cart = new Cart();
        cart.setUuid(UUID.fromString(cartId));

        return cartRepository.save(cart);
    }

    private void clearCart(Cart cart) {
        cart.getCartItems()
                .forEach(cartItem -> cartItem.setCart(null));

        cartRepository.delete(cart);
    }

    private boolean isDiscountPriceValid(Product product) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return product.getDiscountPriceEndDate() != null
                && product.getDiscountPriceEndDate().isAfter(currentDateTime);
    }

}