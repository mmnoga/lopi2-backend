package com.liftoff.project.service.impl;

import com.liftoff.project.controller.cart.response.CartItemResponseDTO;
import com.liftoff.project.controller.cart.response.CartResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.Session;
import com.liftoff.project.repository.CartItemRepository;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.AuthCartService;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.ProductService;
import com.liftoff.project.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthCartServiceImpl implements AuthCartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final SessionService sessionService;
    private final CartMapper cartMapper;
    private static final String CART_NOT_FOUND = "Cart not found";

    @Override
    public String findCartIdByUsername(String username) {
        return cartRepository.findByUserUsername(username)
                .map(Cart::getUuid)
                .map(UUID::toString)
                .orElse(null);
    }

    @Override
    @Transactional
    public String createCartForUser(String username) {
        String cartId = UUID.randomUUID().toString();

        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setUuid(UUID.fromString(cartId));
                    cart.setTotalPrice(0.0);
                    cart.setTotalQuantity(0);

                    Session session = sessionService
                            .createSession(cart.getUuid(), Instant.now().plus(Duration.ofSeconds(86400)));
                    cart.setSession(session);

                    cartRepository.save(cart);
                });

        return cartId;
    }

    @Override
    @Transactional
    public CartResponseDTO processCartForUser(String username, UUID productUid, int quantity) {
        String cartId = findCartIdByUsername(username);

        if (cartId == null) {
            cartId = createCartForUser(username);
        }

        try {
            Cart cart = cartRepository
                    .findByUuid(UUID.fromString(cartId))
                    .orElseThrow(() -> new BusinessException(CART_NOT_FOUND));

            Product product = productService.getProductEntityByUuid(productUid);

            if (!cartService
                    .hasProductEnoughQuantity(product, quantity, cart)) {
                throw new BusinessException("Not enough quantity of product with UUID: "
                        + product.getUId(), HttpStatus.BAD_REQUEST);
            }

            Cart savedCart = cartService.addProductToCart(cart, product, quantity);

            return cartMapper
                    .mapCartToCartResponseDTO(savedCart);

        } catch (BusinessException ex) {
            throw new BusinessException(CART_NOT_FOUND);
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
            throw new BusinessException("Cart not found for user: " + username);
        }

        CartResponseDTO cartResponse = new CartResponseDTO();
        cartResponse.setUuid(cart.getUuid());

        List<CartItemResponseDTO> cartItemResponseList = cart.getCartItems().stream()
                .map(cartMapper::mapCartItemToCartItemResponseDTO)
                .toList();

        cartResponse.setCartItems(cartItemResponseList);
        cartResponse.setTotalPrice(cart.getTotalPrice());
        cartResponse.setTotalQuantity(cart.getTotalQuantity());

        return cartResponse;
    }

    @Override
    @Transactional
    public void clearCartForUser(String username) {
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new BusinessException(CART_NOT_FOUND));

        List<CartItem> cartItems = cart.getCartItems();

        for (CartItem cartItem : cartItems) {
            cartItem.setCart(null);
        }

        cartItems.clear();
        cart.setTotalPrice(0.0);
        cart.setTotalQuantity(0);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCartProductByUuidForUser(UUID productUuid, String username) {
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new BusinessException("Cart not found for user: " + username));

        CartItem cartItemToRemove = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getUId().equals(productUuid))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Product not found in the cart"));

        cartItemToRemove.setCart(null);
        cart.getCartItems().remove(cartItemToRemove);

        Cart finalCart = cartService.calculateTotalPriceAndTotalQuantity(cart);

        cartItemRepository.delete(cartItemToRemove);
        cartRepository.save(finalCart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartForUser(UUID productUuid, int quantity, String username) {
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        String cartId = findCartIdByUsername(username);

        if (cartId == null) {
            cartId = createCartForUser(username);
        }

        try {
            Cart cart = cartRepository
                    .findByUuid(UUID.fromString(cartId))
                    .orElseThrow(() -> new BusinessException(CART_NOT_FOUND));

            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getUId().equals(productUuid))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Product not found in the cart"));

            if (!cartService.hasProductEnoughQuantityForEdit(cartItem.getProduct(), quantity)) {
                throw new BusinessException(
                        "Insufficient quantity of product with UUID: " + productUuid + " in stock");
            }

            cartItem.setQuantity(quantity);

            Cart updatedCart = cartService.calculateTotalPriceAndTotalQuantity(cart);

            updatedCart.setUpdatedAt(Instant.now());

            Cart savedCart = cartRepository.save(updatedCart);

            return cartMapper.mapCartToCartResponseDTO(savedCart);

        } catch (BusinessException ex) {
            throw new BusinessException(CART_NOT_FOUND);
        }
    }

}