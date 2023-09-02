package com.liftoff.project.controller.order.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryMethodResponseDTO {

    private String name;
    private String description;
    private Double cost;

}
