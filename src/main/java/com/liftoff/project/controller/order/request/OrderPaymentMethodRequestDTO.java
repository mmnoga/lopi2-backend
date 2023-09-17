package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPaymentMethodRequestDTO {

    @NotBlank(message = "PaymentMethodName cannot be blank")
    @Size(max = 30, message = "PaymentMethodName cannot exceed 30 characters")
    private String paymentMethodName;

}
