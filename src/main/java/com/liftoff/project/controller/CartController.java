package com.liftoff.project.controller;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "API endpoints for managing shopping carts")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/add")
    @Operation(summary = "Add a product to the shopping cart for unauthenticated user")
    public ResponseEntity<String> addToCart(
            @RequestParam UUID productUuid,
            HttpServletRequest request,
            HttpServletResponse response
            ) {
        Cart cart = cartService.getOrCreateCart(request, response);

        Product product = productService.getProductEntityByUuid(productUuid);

        cart.getProducts().add(product);
        cart.setTotalPrice(cart.getTotalPrice() + product.getRegularPrice());
        cart.setTotalQuantity(cart.getTotalQuantity() + 1);

        cartService.saveCart(cart);

        return ResponseEntity.ok("Product added to cart");
    }

    @GetMapping("/products")
    @Operation(summary = "Get products from the shopping cart for unauthenticated user")
    public ResponseEntity<CartResponseDTO> getCartProducts(HttpServletRequest request) {
        Cart cart = cartService.getOrCreateCart(request, null);

        return ResponseEntity.ok(cart);
    }

}
