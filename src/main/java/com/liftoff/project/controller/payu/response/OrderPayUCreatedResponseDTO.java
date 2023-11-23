package com.liftoff.project.controller.payu.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderPayUCreatedResponseDTO {

    private UUID uuid;
    private String statusCode;
    private String redirectUri;
    private String orderId;
    private String extOrderId;

}
