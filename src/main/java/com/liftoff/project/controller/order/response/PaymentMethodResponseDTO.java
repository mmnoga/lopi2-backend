package com.liftoff.project.controller.order.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodResponseDTO {

    private String name;
    private String description;

}
