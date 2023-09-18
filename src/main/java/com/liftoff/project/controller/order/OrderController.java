package com.liftoff.project.controller.order;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
@AllArgsConstructor
@Tag(name = "Orders")
public class OrderController {


    private final OrderService orderService;

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/add")
    @Operation(summary = "Add an order from cart")
    public ResponseEntity<OrderSummaryResponseDTO> createOrder(
            @RequestParam UUID cartUuid) {
        return new ResponseEntity<OrderSummaryResponseDTO>(orderService.addOrder(cartUuid), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit order")
    public ResponseEntity<OrderDetailsResponseDTO> editOrder(
            @Valid @RequestBody OrderRequestDTO orderRequest,
            @RequestParam UUID orderUuid) {

        return ResponseEntity.ok(orderService.editOrder(orderRequest, orderUuid));
    }


    @PutMapping("/change-delivery-method")
    @Operation(summary = "Change an order delivery method")
    public ResponseEntity<OrderDetailsResponseDTO> changeOrderDeliveryMethod(
            @Valid @RequestBody OrderDeliveryMethodRequestDTO orderChangeRequestDTO,
            @RequestParam UUID uuid) {

        return ResponseEntity.ok(orderService.changeOrderDeliveryMethod(orderChangeRequestDTO, uuid));
    }



    @PutMapping("/change-payment-method")
    @Operation(summary = "Change an order delivery payment")
//    public ResponseEntity<OrderDetailsResponseDTO> changeOrderPaymentMethod(
//            @Valid @RequestBody OrderPaymentMethodRequestDTO paymentMethodRequestDTO,
//            @RequestParam UUID uuid) {
    public ResponseEntity<OrderDetailsResponseDTO> changeOrderPaymentMethod(
            @RequestParam String paymentMethod,
            @RequestParam UUID uuid) {

        return ResponseEntity.ok(orderService.changeOrderPaymentMethod(paymentMethod, uuid));

    }

    @PostMapping("/create")
    public ResponseEntity<OrderDetailsResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequest,
            @RequestParam UUID cartUuid) {
        OrderDetailsResponseDTO createdOrder = orderService
                .createOrder(orderRequest, cartUuid);

        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/summary")
    public ResponseEntity<OrderSummaryListResponseDTO> getOrdersSummary() {
        return ResponseEntity.ok(orderService.getAllOrdersSummary());
    }

    @GetMapping("/details")
    public ResponseEntity<OrderDetailsListResponseDTO> getOrdersDetails() {
        return ResponseEntity.ok(orderService.getAllOrdersDetails());
    }
}
