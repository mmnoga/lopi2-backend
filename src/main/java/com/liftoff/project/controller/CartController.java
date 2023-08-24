package com.liftoff.project.controller;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.LoginAuthenticationException;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@Tag(name = "Shopping Cart", description = "API endpoints for managing shopping carts")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final CartMapper cartMapper;

    @PostMapping("/add")
    @Operation(summary = "Add a product to the shopping cart")
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

        return ResponseEntity.ok("Product " + productUuid + " added to cart");
    }

    @GetMapping()
    @Operation(summary = "View the shopping cart")
    public ResponseEntity<CartResponseDTO> getCartProducts(HttpServletRequest request) {
        Cart cart = cartService.getOrCreateCart(request, null);

        CartResponseDTO cartResponseDTO = cartMapper.mapEntityToResponse(cart);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear the shopping cart")
    public ResponseEntity<String> clearCart(HttpServletRequest request) {
        cartService.clearCart(request);

        return ResponseEntity.ok("Cart has been cleared");
    }

    @PostMapping("/add-auth")
    @Operation(summary = "Add a product to the authenticated user's shopping cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addToCartForAuth(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID productUid) {
        if (userDetails == null) {
            throw new LoginAuthenticationException("User is not authenticated");
        }

        String username = userDetails.getUsername();

        String cartId = cartService.findCartIdByUsername(username);

        if (cartId == null) {
            cartId = UUID.randomUUID().toString();
            cartService.createCartForUser(username, cartId);
        }

        String addToCartResponse = cartService.addToCart(cartId, productUid);

        return ResponseEntity.ok(addToCartResponse);
    }

    @GetMapping("/view-auth")
    @Operation(summary = "View authenticated user's shopping cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> viewCartForAuth(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new LoginAuthenticationException("User is not authenticated");
        }

        String username = userDetails.getUsername();

        String cartId = cartService.findCartIdByUsername(username);

        if (cartId == null) {
            return ResponseEntity.ok(new CartResponseDTO());
        }

        CartResponseDTO cartResponse = cartService.getCart(cartId);

        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/view-cart-auth")
    @Operation(summary = "Clear authenticated user's shopping cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> clearShoppingCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new LoginAuthenticationException("User is not authenticated");
        }

        String username = userDetails.getUsername();

        String cartId = cartService.findCartIdByUsername(username);
        cartService.clearUserCart(cartId);

        return ResponseEntity.ok("Shopping cart has been cleared");
    }


}
