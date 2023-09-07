package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDeliveryMethodRequestDTO {

    @NotBlank(message = "DeliveryMethodName cannot be blank")
    @Size(max = 30, message = "PostalCode cannot exceed 30 characters")
    private String deliveryMethodName;

}
