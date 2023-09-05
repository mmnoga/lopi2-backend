package com.liftoff.project.controller.order.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPaymentMethodRequestDTO {

    private String paymentMethodName;

}
