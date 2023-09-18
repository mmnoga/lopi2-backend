package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentMethodRequestDTO {

    @NotBlank(message = "PaymentMethodName cannot be blank")
    @Size(max = 30, message = "PostalCode cannot exceed 30 characters")
    private String paymentMethodName;

}
