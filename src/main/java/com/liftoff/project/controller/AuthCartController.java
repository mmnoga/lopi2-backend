package com.liftoff.project.controller;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.service.AuthCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/auth-cart")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart for Auth", description = "Managing shopping carts for auth users")
public class AuthCartController {

    private final AuthCartService authCartService;

    @PostMapping("/add")
    @Operation(summary = "Add a product to the authenticated user's shopping cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addToCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID productUid,
            @RequestParam(required = false, defaultValue = "1") int quantity) {
        String username = userDetails.getUsername();

        String processCartResponse = authCartService
                .processCartForUser(username, productUid, quantity);

        return ResponseEntity.ok(processCartResponse);
    }

    @GetMapping("/view")
    @Operation(summary = "View authenticated user's shopping cart",
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

    @DeleteMapping("/clear")
    @Operation(summary = "Clear authenticated user's shopping cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> clearCartForAuthenticated(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization")
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        authCartService.clearCartForUser(username);

        return ResponseEntity.ok("Shopping cart has been cleared");
    }

}