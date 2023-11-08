package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderSummaryToSendRequestDTO {

    @NotNull(message = "Order UUID is required")
    private UUID orderUuid;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Email must be valid an email address")
    @NotBlank(message = "Email cannot be empty")
    private String email;

}