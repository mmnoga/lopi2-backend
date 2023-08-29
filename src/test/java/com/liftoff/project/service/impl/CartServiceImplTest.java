package com.liftoff.project.service.impl;

import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.Product;
import com.liftoff.project.repository.CartItemRepository;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.service.CookieService;
import com.liftoff.project.service.ProductService;
import com.liftoff.project.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CookieService cookieService;

    @Mock
    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldAddProductToCart() {
        // given
        UUID cartUuid = UUID.randomUUID();
        String cartId = cartUuid.toString();
        UUID productUuid = UUID.randomUUID();

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);
        product.setRegularPrice(20.0);

        Cart cart = new Cart();
        cart.setUuid(cartUuid);
        List<CartItem> cartItems = new ArrayList<>();
        cart.setCartItems(cartItems);

        when(cartRepository.findByUuid(cartUuid)).thenReturn(Optional.of(cart));
        when(productService.getProductEntityByUuid(productUuid)).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartServiceImpl cartService =
                new CartServiceImpl(cartRepository, cartItemRepository, cookieService, productService, sessionService);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(cookieService.getCookieValue(any(), eq(request))).thenReturn(cartId);

        // when
        cartService.processCart(productUuid, 1, request, response);

        // then
        assertEquals(20.0, cart.getTotalPrice());
        assertEquals(1, cart.getTotalQuantity());
    }

}