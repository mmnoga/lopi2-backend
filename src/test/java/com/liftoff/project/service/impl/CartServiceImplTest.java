package com.liftoff.project.service.impl;

import com.liftoff.project.exception.CartNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.exception.ProductOutOfStockException;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

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
        List<Product> products = new ArrayList<>();
        cart.setProducts(products);

        when(cartRepository.findByUuid(cartUuid)).thenReturn(Optional.of(cart));
        when(productService.getProductEntityByUuid(productUuid)).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // when
        String result = cartService.addToCart(cartId, productUuid);

        // then
        assertEquals("Product " + productUuid + " added to cart " + cartUuid, result);
        assertEquals(1, cart.getProducts().size());
        assertEquals(9, product.getQuantity());
        assertEquals(20.0, cart.getTotalPrice());
        assertEquals(1, cart.getTotalQuantity());
    }

    @Test
    public void shouldThrowProductNotFoundExceptionWhenAddNoExistingProduct() {
        UUID cartUuid = UUID.randomUUID();
        String cartId = cartUuid.toString();
        UUID productUuid = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setUuid(cartUuid);

        when(cartRepository.findByUuid(cartUuid)).thenReturn(Optional.of(cart));
        when(productService.getProductEntityByUuid(productUuid)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> cartService.addToCart(cartId, productUuid));
        verify(cartRepository, times(0)).save(any());
    }

    @Test
    public void shouldThrowProductOutOfStockWhenAddOutOfStockProduct() {
        UUID cartUuid = UUID.randomUUID();
        String cartId = cartUuid.toString();
        UUID productUuid = UUID.randomUUID();
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(0);

        Cart cart = new Cart();
        cart.setUuid(cartUuid);

        when(cartRepository.findByUuid(cartUuid)).thenReturn(Optional.of(cart));
        when(productService.getProductEntityByUuid(productUuid)).thenReturn(product);

        assertThrows(ProductOutOfStockException.class, () -> cartService.addToCart(cartId, productUuid));
        verify(cartRepository, times(0)).save(any());
    }

    @Test
    public void shouldThrowCartNotFoundExceptionWhenAddProductToNoExistingCart() {
        UUID cartUuid = UUID.randomUUID();
        String cartId = cartUuid.toString();
        UUID productUuid = UUID.randomUUID();

        when(cartRepository.findByUuid(cartUuid)).thenReturn(Optional.empty());


        assertThrows(CartNotFoundException.class, () -> cartService.addToCart(cartId, productUuid));
        verify(productService, times(0)).getProductEntityByUuid(any());
    }
}