package com.liftoff.project.controller.order.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderChangeRequestDTO {

    private UUID uuid;
    private String deliveryMethodName;
    private String paymentMethodName;


}
