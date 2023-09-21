package com.liftoff.project.controller.cart;

import com.liftoff.project.controller.cart.response.CartResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping
    @Operation(summary = "Add a specified quantity of product")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestParam UUID productUuid,
            @RequestParam(defaultValue = "1") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }

        Cart savedCart = cartService
                .processCart(productUuid, quantity, request, response);

        CartResponseDTO cartResponseDTO = cartMapper.mapCartToCartResponseDTO(savedCart);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @GetMapping
    @Operation(summary = "View a cart")
    public ResponseEntity<CartResponseDTO> getCart(
            HttpServletRequest request,
            HttpServletResponse response) {

        Cart cart = cartService
                .getCartByCookieOrCreateNewCart(request, response);

        CartResponseDTO cartResponseDTO = cartMapper
                .mapCartToCartResponseDTO(cart);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @PutMapping
    @Operation(summary = "Update a product quantity")
    public ResponseEntity<CartResponseDTO> updateCart(
            @RequestParam UUID productUuid,
            @RequestParam(defaultValue = "1") @Min(1) int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {

        CartResponseDTO cartResponseDTO = cartService
                .updateCart(productUuid, quantity, request, response);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @DeleteMapping
    @Operation(summary = "Clear a cart contents")
    public ResponseEntity<String> clearCart(
            HttpServletRequest request) {

        cartService.clearCart(request);

        return ResponseEntity.ok("Cart has been cleared");
    }

    @DeleteMapping("/{productUuid}")
    @Operation(summary = "Remove a product by its uuid")
    public ResponseEntity<String> removeProductFromCart(
            @PathVariable UUID productUuid,
            HttpServletRequest request) {

        cartService.removeProduct(productUuid, request);

        return ResponseEntity.ok("Product has been removed from cart");
    }

}