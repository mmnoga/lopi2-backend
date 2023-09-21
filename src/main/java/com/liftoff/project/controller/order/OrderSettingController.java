package com.liftoff.project.controller.order;

import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.controller.order.response.PaymentMethodListResponseDTO;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.service.DeliveryMethodService;
import com.liftoff.project.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders-setting")
@AllArgsConstructor
@Tag(name = "Order Payment & Delivery Methods, Order Status")
public class OrderSettingController {

    private final PaymentMethodService paymentMethodService;
    private final DeliveryMethodService deliveryMethodService;

    @GetMapping("/payment-method")
    @Operation(summary = "Get a list of payment methods")
    public ResponseEntity<PaymentMethodListResponseDTO> getPaymentMethods() {

        return ResponseEntity.ok(paymentMethodService.getPaymentMethods());
    }

    @GetMapping("/delivery-method")
    @Operation(summary = "Get a list of delivery methods")
    public List<DeliveryMethodResponseDTO> getDeliveryMethods() {
        return deliveryMethodService.getDeliveryMethods();
    }

    @GetMapping("/order-status")
    @Operation(summary = "Get a list of order statuses")
    public OrderStatus[] getOrderStatuses() {
        return OrderStatus.values();
    }

}