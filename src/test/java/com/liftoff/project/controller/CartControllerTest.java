package com.liftoff.project.controller;

import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class CartControllerTest {

    @Value("${cart.cookie.name}")
    private String CART_ID_COOKIE_NAME;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    @Test
    void shouldAddProductToCart() throws Exception {
        // given
        UUID productUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        Product product = new Product();
        product.setUId(productUuid);
        product.setRegularPrice(10.99);

        Cart cart = new Cart();
        when(cartService.getOrCreateCart(any(), any())).thenReturn(cart);

        when(productService.getProductEntityByUuid(productUuid)).thenReturn(product);

        // when&then
        mockMvc.perform(post("/api/cart/add")
                        .param("productUuid", productUuid.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddTwoProductsToCart() throws Exception {
        // given
        UUID productUuid1 = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        UUID productUuid2 = UUID.fromString("f79cd22c-a65e-45b6-bb3f-8234ef54cdef");

        Product product1 = new Product();
        product1.setUId(productUuid1);
        product1.setRegularPrice(10.99);

        Product product2 = new Product();
        product2.setUId(productUuid2);
        product2.setRegularPrice(15.49);

        Cart cart = new Cart();
        cart.setUuid(UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(CART_ID_COOKIE_NAME, cart.getUuid().toString()));

        when(cartService.getOrCreateCart(any(), any())).thenReturn(cart);
        when(productService.getProductEntityByUuid(productUuid1)).thenReturn(product1);
        when(productService.getProductEntityByUuid(productUuid2)).thenReturn(product2);

        // when/then
        mockMvc.perform(post("/api/cart/add").param("productUuid", productUuid1.toString())
                        .requestAttr("javax.servlet.http.Cookie",
                                new Cookie[]{new Cookie(CART_ID_COOKIE_NAME, cart.getUuid().toString())}))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/cart/add").param("productUuid", productUuid2.toString())
                        .requestAttr("javax.servlet.http.Cookie",
                                new Cookie[]{new Cookie(CART_ID_COOKIE_NAME, cart.getUuid().toString())}))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCartProducts() throws Exception {
        //given
        UUID cartUuid = UUID.fromString("f28bd377-3a7d-44fe-bbc9-adeb3bea03fa");
        Cart cart = new Cart();
        cart.setUuid(cartUuid);

        when(cartService.getOrCreateCart(any(HttpServletRequest.class), isNull())).thenReturn(cart);

        // when&then
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(cartUuid.toString()));
    }

    @Test
    void shouldClearCart() throws Exception {
        // given
        UUID productUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        Product product = new Product();
        product.setUId(productUuid);
        product.setRegularPrice(10.99);

        Cart cart = new Cart();
        cart.setProducts(List.of(product));

        when(cartService.getOrCreateCart(any(), any())).thenReturn(cart);

        // when&then
        mockMvc.perform(delete("/api/cart/clear"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart has been cleared"));

        assertEquals(0.0, cart.getTotalPrice(), 0.001);
        assertEquals(0, cart.getTotalQuantity());
    }

}