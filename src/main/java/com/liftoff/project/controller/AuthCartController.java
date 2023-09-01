package com.liftoff.project.controller;

import com.liftoff.project.controller.request.CartRequestDTO;
import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.service.AuthCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth-cart")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart for Auth", description = "Shopping Cart management for auth users")
public class AuthCartController {

    private final AuthCartService authCartService;

    @PostMapping("/add")
    @Operation(summary = "Add specified quantity of product by ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
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

    @GetMapping("/view")
    @Operation(summary = "View cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> getCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        CartResponseDTO cartResponse = authCartService
                .viewCartForUser(username);

        return ResponseEntity.ok(cartResponse);
    }

    @PutMapping
    @Operation(summary = "Update products quantity by ID and new quantity",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> updateCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid List<CartRequestDTO> cartRequestDTOList) {
        String username = userDetails.getUsername();

        CartResponseDTO cartResponseDTO = authCartService
                .updateCartForUser(cartRequestDTOList, username);

        return ResponseEntity.ok(cartResponseDTO);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart contents",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> clearCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        authCartService.clearCartForUser(username);

        return ResponseEntity.ok("Shopping cart has been cleared");
    }

    @DeleteMapping("/{productUuid}")
    @Operation(summary = "Remove product by ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> removeProductFromCart(
            @PathVariable UUID productUuid,
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        authCartService.deleteCartProductByUuidForUser(productUuid, username);

        return ResponseEntity.ok("Product has been removed from cart");
    }

}