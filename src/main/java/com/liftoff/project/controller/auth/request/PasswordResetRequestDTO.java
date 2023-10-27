package com.liftoff.project.controller.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {

    @NotBlank(message = "encodedUsername cannot be empty")
    private String encodedUsername;

    @NotBlank(message = "tokenValue cannot be empty")
    private String tokenValue;

    @NotBlank(message = "encodedPassword cannot be empty")
    private String encodedPassword;

}