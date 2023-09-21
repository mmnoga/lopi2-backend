package com.liftoff.project.controller.order;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.service.DeliveryMethodService;
import com.liftoff.project.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders-setting")
@AllArgsConstructor
@Tag(name = "Order Payment & Delivery Methods, Order Status <Admin>")
public class OrderSettingAuthController {

    private final PaymentMethodService paymentMethodService;
    private final DeliveryMethodService deliveryMethodService;

    @PostMapping("/payment-method")
    @Operation(summary = "Add a payment method",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<PaymentMethodResponseDTO> addPaymentMethod(
            @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        return ResponseEntity.ok(paymentMethodService.addPaymentMethod(paymentMethodRequestDTO));
    }

    @DeleteMapping("/payment-method/{name}")
    @Operation(summary = "Delete a payment method by its name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<String> deletePaymentMethodByName(@PathVariable String name) {
        paymentMethodService.deletePaymentMethodByName(name);
        return ResponseEntity.ok("Payment method deleted successfully");
    }

    @PutMapping("/payment-method/{name}")
    @Operation(summary = "Update a payment method by its name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethodByName(
            @PathVariable String name,
            @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        return ResponseEntity.ok(paymentMethodService.editPaymentMethod(name, paymentMethodRequestDTO));
    }

    @PostMapping("/delivery-method")
    @Operation(summary = "Add a delivery method",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public DeliveryMethodResponseDTO addDeliveryMethod(
            @Valid @RequestBody DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        return deliveryMethodService.addDeliveryMethod(deliveryMethodRequestDTO);
    }

    @DeleteMapping("/delivery-method/{name}")
    @Operation(summary = "Delete a delivery method by its name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<String> deleteDeliveryMethodByName(@PathVariable String name) {
        deliveryMethodService.deleteDeliveryMethodByName(name);
        return ResponseEntity.ok("Delivery method deleted successfully");
    }

    @PutMapping("/delivery-method/{name}")
    @Operation(summary = "Update a delivery method by its name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public DeliveryMethodResponseDTO updateDeliveryMethodByName(
            @PathVariable String name,
            @Valid @RequestBody DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        return deliveryMethodService.editDeliveryMethod(name, deliveryMethodRequestDTO);
    }

}