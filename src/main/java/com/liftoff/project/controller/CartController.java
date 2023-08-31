package com.liftoff.project.controller;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Shopping Cart", description = "Managing shopping carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping("/add")
    @Operation(summary = "Add a product to the shopping cart")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestParam UUID productUuid,
            @RequestParam(defaultValue = "1") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {
        CartResponseDTO cartResponseDTO = cartService
                .processCart(productUuid, quantity, request, response);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @GetMapping()
    @Operation(summary = "View the shopping cart")
    public ResponseEntity<CartResponseDTO> getCart(HttpServletRequest request) {
        Cart cart = cartService
                .getCartByCookieOrCreateNewCart(request, null);

        CartResponseDTO cartResponseDTO = cartMapper
                .mapEntityToResponse(cart);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear the shopping cart")
    public ResponseEntity<String> clearCart(HttpServletRequest request) {
        cartService.clearCart(request);

        return ResponseEntity.ok("Cart has been cleared");
    }

}