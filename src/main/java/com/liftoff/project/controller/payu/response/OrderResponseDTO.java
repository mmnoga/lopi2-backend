package com.liftoff.project.controller.payu.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {


    @NotNull(message = "OrderPayU StatusCode is required")
    private OrderResponseStatusDTO status;
    @NotNull(message = "OrderPayU Redirect URI is required")
    private String redirectUri;
    @NotNull(message = "OrderPayU orderUuid is required")
    private String orderId;
    @NotNull(message = "OrderPayU extOrderUuid is required")
    private String extOrderId;

}
