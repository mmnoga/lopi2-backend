package com.liftoff.project.controller.payu.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseStatusDTO {

    @NotNull(message = "OrderPayU Status Code is required")
    private String statusCode;

}
