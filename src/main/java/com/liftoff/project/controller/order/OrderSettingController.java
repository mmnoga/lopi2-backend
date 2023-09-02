package com.liftoff.project.controller.order;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.service.DeliveryMethodService;
import com.liftoff.project.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders-setting")
@CrossOrigin("*")
@AllArgsConstructor
@Tag(name = "Order Payment & Delivery Methods, Order Status")
public class OrderSettingController {

    private final PaymentMethodService paymentMethodService;
    private final DeliveryMethodService deliveryMethodService;

    @GetMapping("/payment-method")
    public List<PaymentMethodResponseDTO> getPaymentMethods() {
        return paymentMethodService.getPaymentMethods();
    }

    @PostMapping("/payment-method")
    public PaymentMethodResponseDTO addPaymentMethod(
            @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        return paymentMethodService.addPaymentMethod(paymentMethodRequestDTO);
    }

    @DeleteMapping("/payment-method/{name}")
    public ResponseEntity<String> deletePaymentMethodByName(@PathVariable String name) {
        paymentMethodService.deletePaymentMethodByName(name);
        return ResponseEntity.ok("Payment method deleted successfully");
    }

    @PutMapping("/payment-method/{name}")
    public PaymentMethodResponseDTO updatePaymentMethodByName(
            @PathVariable String name,
            @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        return paymentMethodService.editPaymentMethod(name, paymentMethodRequestDTO);
    }

    @GetMapping("/delivery-method")
    public List<DeliveryMethodResponseDTO> getDeliveryMethods() {
        return deliveryMethodService.getDeliveryMethods();
    }

    @PostMapping("/delivery-method")
    public DeliveryMethodResponseDTO addDeliveryMethod(
            @Valid @RequestBody DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        return deliveryMethodService.addDeliveryMethod(deliveryMethodRequestDTO);
    }

    @DeleteMapping("/delivery-method/{name}")
    public ResponseEntity<String> deleteDeliveryMethodByName(@PathVariable String name) {
        deliveryMethodService.deleteDeliveryMethodByName(name);
        return ResponseEntity.ok("Delivery method deleted successfully");
    }

    @PutMapping("/delivery-method/{name}")
    public DeliveryMethodResponseDTO updateDeliveryMethodByName(
            @PathVariable String name,
            @Valid @RequestBody DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        return deliveryMethodService.editDeliveryMethod(name, deliveryMethodRequestDTO);
    }

    @GetMapping("/order-status")
    public OrderStatus[] getOrderStatuses() {
        return OrderStatus.values();
    }

}