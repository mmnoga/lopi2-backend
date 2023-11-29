package com.liftoff.project.controller.payu.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private StatusDTO status;
    private String redirectUri;
    private String orderId;
    private String extOrderId;

}