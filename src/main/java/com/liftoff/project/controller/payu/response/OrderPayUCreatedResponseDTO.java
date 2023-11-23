package com.liftoff.project.controller.payu.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayUCreatedResponseDTO {

    private UUID uuid;
    private OrderResponseStatusDTO status;
    private String redirectUri;
    private String orderId;
    private String extOrderId;

}
