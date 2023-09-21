package com.liftoff.project.controller.cart;

import com.liftoff.project.configuration.security.annotations.HasUserRole;
import com.liftoff.project.controller.cart.response.CartResponseDTO;
import com.liftoff.project.service.AuthCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/auth-cart")
@RequiredArgsConstructor
@Tag(name = "Cart <User>")
public class CartAuthController {

    private final AuthCartService authCartService;

    @PostMapping
    @Operation(summary = "Add a specified quantity of product",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasUserRole
    public ResponseEntity<CartResponseDTO> addToCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID productUid,
            @RequestParam(required = false, defaultValue = "1") int quantity) {
        String username = userDetails.getUsername();

        CartResponseDTO cartResponseDTO = authCartService
                .processCartForUser(username, productUid, quantity);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @GetMapping
    @Operation(summary = "View a cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasUserRole
    public ResponseEntity<CartResponseDTO> getCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        CartResponseDTO cartResponse = authCartService
                .viewCartForUser(username);

        return ResponseEntity.ok(cartResponse);
    }

    @PutMapping
    @Operation(summary = "Update a product quantity",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasUserRole
    public ResponseEntity<CartResponseDTO> updateCartForAuthenticated(
            @RequestParam UUID productUuid,
            @RequestParam(defaultValue = "1") @Min(1) int quantity,
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        CartResponseDTO cartResponseDTO = authCartService
                .updateCartForUser(productUuid, quantity, username);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @DeleteMapping
    @Operation(summary = "Clear a cart contents",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasUserRole
    public ResponseEntity<String> clearCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        authCartService.clearCartForUser(username);

        return ResponseEntity.ok("Shopping cart has been cleared");
    }

    @DeleteMapping("/{productUuid}")
    @Operation(summary = "Remove a product by its uuid",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasUserRole
    public ResponseEntity<String> removeProductFromCart(
            @PathVariable UUID productUuid,
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        authCartService.deleteCartProductByUuidForUser(productUuid, username);

        return ResponseEntity.ok("Product has been removed from cart");
    }

}