package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderSummaryForUserRequestDTO {

    @NotNull(message = "Order UUID is required")
    private UUID orderUuid;

}