package com.liftoff.project.controller;

import com.liftoff.project.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Shopping Order", description = "Managing shopping orders")
public class OrderController {


    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Add an order from card (basket)")
    public ResponseEntity<String> createOrder(@RequestParam UUID cartUuid) {


        orderService.processOrder(cartUuid);

        return ResponseEntity.ok("Cart " + cartUuid + " added to Order");
    }


}
