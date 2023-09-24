package com.liftoff.project.controller.order;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.configuration.security.annotations.HasAnyRole;
import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth-orders")
@AllArgsConstructor
@Tag(name = "Orders <Admin OR User>")
public class OrderAuthController {

    private final OrderService orderService;

    @GetMapping("/summary")
    @Operation(summary = "Get a list of summary orders",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderSummaryListResponseDTO> getOrdersSummary() {
        return ResponseEntity.ok(orderService.getAllOrdersSummary());
    }

    @GetMapping("/details")
    @Operation(summary = "Get a list of details orders",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderDetailsListResponseDTO> getOrdersDetails() {
        return ResponseEntity.ok(orderService.getAllOrdersDetails());
    }

    @PostMapping("/add")
    @Operation(summary = "Add an order from cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderSummaryResponseDTO> createOrder(
            @RequestParam UUID cartUuid) {
        return new ResponseEntity<OrderSummaryResponseDTO>(orderService.addOrder(cartUuid), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update an order",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderDetailsResponseDTO> editOrder(
            @Valid @RequestBody OrderRequestDTO orderRequest,
            @RequestParam UUID orderUuid) {

        return ResponseEntity.ok(orderService.editOrder(orderRequest, orderUuid));
    }

    @PutMapping("/change-delivery-method")
    @Operation(summary = "Change an order delivery method",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderDetailsResponseDTO> changeOrderDeliveryMethod(
            @Valid @RequestBody OrderDeliveryMethodRequestDTO orderChangeRequestDTO,
            @RequestParam UUID uuid) {

        return ResponseEntity.ok(orderService.changeOrderDeliveryMethod(orderChangeRequestDTO, uuid));
    }

    @PutMapping("/change-payment-method")
    @Operation(summary = "Change an order delivery payment",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderDetailsResponseDTO> changeOrderPaymentMethod(
            @RequestParam String paymentMethod,
            @RequestParam UUID uuid) {

        return ResponseEntity.ok(orderService.changeOrderPaymentMethod(paymentMethod, uuid));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new order",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAnyRole
    public ResponseEntity<OrderDetailsResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequest,
            @RequestParam UUID cartUuid) {
        OrderDetailsResponseDTO createdOrder = orderService
                .createOrder(orderRequest, cartUuid);

        return ResponseEntity.ok(createdOrder);
    }


    @GetMapping("/{orderUuid}")
    @Operation(summary = "Get Order by UUID",  security = @SecurityRequirement(name = "bearerAuth"),
            description = "Retrieves order information by its UUID.")
    @HasAnyRole
    public ResponseEntity<OrderDetailsResponseDTO> getOrderByUuid(@PathVariable UUID orderUuid) {
        OrderDetailsResponseDTO orderRequestDTO = orderService.getOrderByUuid(orderUuid);
        if (orderRequestDTO != null) {
            return ResponseEntity.ok(orderRequestDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}