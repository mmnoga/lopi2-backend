package com.liftoff.project.controller;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.product.ProductNotEnoughQuantityException;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
//@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Shopping Cart management")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;


    @PostMapping("/add")
    @Operation(summary = "Add specified quantity of product by ID")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestParam UUID productUuid,
            @RequestParam(defaultValue = "1") int quantity,
            HttpServletRequest request,
            HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        if (quantity <= 0) {
            throw new ProductNotEnoughQuantityException("Quantity must be greater than 0");
        }

        Cart savedCart = cartService
                .processCart(productUuid, quantity, request, response);

        CartResponseDTO cartResponseDTO = cartMapper.mapCartToCartResponseDTO(savedCart);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @GetMapping()
    @Operation(summary = "View cart")
    public ResponseEntity<CartResponseDTO> getCart(
            HttpServletRequest request,
            HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        Cart cart = cartService
                .getCartByCookieOrCreateNewCart(request, null);

        CartResponseDTO cartResponseDTO = cartMapper
                .mapCartToCartResponseDTO(cart);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @CrossOrigin(
            origins = "http://localhost:4000",
            methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT},
            allowCredentials = "true"
    )
    @PutMapping
    @Operation(summary = "Update products quantity by ID and new quantity")
    public ResponseEntity<CartResponseDTO> updateCart(
            @RequestParam UUID productUuid,
            @RequestParam(defaultValue = "1") @Min(1) int quantity,
            HttpServletRequest request) {

        CartResponseDTO cartResponseDTO = cartService
                .updateCart(productUuid, quantity, request);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @CrossOrigin(
            origins = "http://localhost:4000",
            methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT},
            allowCredentials = "true"
    )
    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart contents")
    public ResponseEntity<String> clearCart(
            HttpServletRequest request) {

        cartService.clearCart(request);

        return ResponseEntity.ok("Cart has been cleared");
    }

    @CrossOrigin(
            origins = "http://localhost:4000",
            methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT},
            allowCredentials = "true"
    )
    @DeleteMapping("/{productUuid}")
    @Operation(summary = "Remove product by ID")
    public ResponseEntity<String> removeProductFromCart(
            @PathVariable UUID productUuid,
            HttpServletRequest request) {

        cartService.removeProduct(productUuid, request);

        return ResponseEntity.ok("Product has been removed from cart");
    }

}