package com.liftoff.project.controller.payu;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;
import com.liftoff.project.service.PayUService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payu")
@RequiredArgsConstructor
@Tag(name = "PayU Payments")
public class PayUController {

    private final PayUService payUService;

    @GetMapping("/token")
    @ResponseBody
    @Operation(summary = "Get an access token")
    public ResponseEntity<PayUAuthResponseDTO> getAccessToken() {

        return ResponseEntity.ok(payUService
                .getAccessToken());
    }

    @GetMapping("/payment-methods")
    @Operation(summary = "Get payment methods",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethods(
            @Parameter(hidden = true)
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        return ResponseEntity.ok(payUService
                .getPaymentMethods(authorizationHeader));
    }

    @PostMapping("/orders")
    @Operation(summary = "Place an order",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderCreatedResponseDTO> submitOrder(
            @Parameter(hidden = true)
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody OrderCreateRequestDTO orderCreateRequestDTO) {

        return ResponseEntity.ok(payUService
                .addOrder(authorizationHeader, orderCreateRequestDTO));
    }

    @PostMapping("/orders/{orderUuid}")
    @Operation(summary = "Process payment for an order",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderCreatedResponseDTO> handlePayment(
            @Parameter(hidden = true)
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable UUID orderUuid,
            HttpServletRequest request) {

        return ResponseEntity.ok(payUService
                .handlePayment(authorizationHeader, orderUuid, request));
    }

    @GetMapping("/shops")
    @Operation(summary = "Get a shop details",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ShopDetailsResponseDTO> getShopDetails(
            @Parameter(hidden = true)
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        return ResponseEntity.ok(payUService
                .getShopDetails(authorizationHeader));
    }

}