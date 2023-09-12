package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryMethodRequestDTO {

    @NotBlank(message = "Name must not be blank")
    @NotNull(message = "Name must not be null")
    private String name;
    private String description;
    private Double cost;

}
