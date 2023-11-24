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
public class StatusDTO {

    @NotNull(message = "statusCode is required")
    private String statusCode;

}