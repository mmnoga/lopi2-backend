package com.liftoff.project.controller.order;

import com.liftoff.project.controller.order.request.OrderChangeRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<String> createOrder(@RequestParam UUID cartUuid) {

        orderService.addOrder(cartUuid);

        return ResponseEntity.ok("Cart " + cartUuid + " added to Order");
    }

    @PostMapping("/edit")
    @Operation(summary = "Edit order")
    public ResponseEntity<String> editOrder(@Valid @RequestBody OrderRequestDTO orderRequest, @RequestParam UUID orderUuid) {

        orderService.editOrder(orderRequest, orderUuid);
        return ResponseEntity.ok("OK delivery method ");
    }


    @PostMapping("/change/delivery/method")
    @Operation(summary = "Change an order delivery method")
    public ResponseEntity<String> changeDeliveryMethod(@Valid @RequestBody OrderChangeRequestDTO orderChangeRequestDTO) {

        orderService.changeDeliveryMethod(orderChangeRequestDTO);
        return ResponseEntity.ok("OK delivery method ");
    }

    @PostMapping("/change/payment/method")
    @Operation(summary = "Change an order delivery payment")
    public ResponseEntity<String> changePaymentMethod(@Valid @RequestBody OrderChangeRequestDTO orderChangeRequestDTO) {

        orderService.changePaymentMethod(orderChangeRequestDTO);
        return ResponseEntity.ok("OK changePaymentMethod ");
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
    public List<OrderSummaryResponseDTO> getOrdersSummary() {
        return orderService.getAllOrdersSummary();
    }

    @GetMapping("/details")
    public List<OrderDetailsResponseDTO> getOrdersDetails() {
        return orderService.getAllOrdersDetails();
    }
}
