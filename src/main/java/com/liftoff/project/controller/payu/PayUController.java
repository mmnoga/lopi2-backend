package com.liftoff.project.controller.payu;

import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderDetailsResponseDTO;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payu")
@RequiredArgsConstructor
@Tag(name = "PayU Payments")
public class PayUController {

    private final PayUService payUService;

    @GetMapping("/payment-methods")
    @Operation(summary = "Get payment methods")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethods() {

        return ResponseEntity.ok(payUService
                .getPaymentMethods());
    }

    @PostMapping("/orders")
    @Operation(summary = "Place an order",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderCreatedResponseDTO> submitOrder(
            @Parameter(hidden = true)
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody OrderCreateRequestDTO orderCreateRequestDTO) {

        return ResponseEntity.ok(payUService
                .addOrder(orderCreateRequestDTO));
    }

    @PostMapping("/orders/{orderUuid}")
    @Operation(summary = "Process payment for an order")
    public ResponseEntity<OrderCreatedResponseDTO> handlePayment(
            @PathVariable UUID orderUuid,
            HttpServletRequest request) {

        return ResponseEntity.ok(payUService
                .handlePayment(orderUuid, request));
    }

    @GetMapping("/shops")
    @Operation(summary = "Get a shop details")
    public ResponseEntity<ShopDetailsResponseDTO> getShopDetails() {

        return ResponseEntity.ok(payUService
                .getShopDetails());
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Get an order details")
    public ResponseEntity<OrderDetailsResponseDTO> getOrderDetails(
            @PathVariable String orderId) {

        return ResponseEntity.ok(payUService
                .getOrderDetails(orderId));
    }

}