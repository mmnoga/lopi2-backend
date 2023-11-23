package com.liftoff.project.controller.payu.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayURequestDTO {

    @NotNull(message = "OrderPayU UUID is required")
    private UUID uuid;
    @NotNull(message = "OrderPayU StatusCode is required")
    private StatusOrderPayURequestDTO status;
    @NotNull(message = "OrderPayU Redirect URI is required")
    private String redirectUri;
    @NotNull(message = "OrderPayU orderUuid is required")
    private String orderId;
    @NotNull(message = "OrderPayU extOrderUuid is required")
    private String extOrderId;


}

