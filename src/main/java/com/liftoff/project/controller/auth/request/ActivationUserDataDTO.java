package com.liftoff.project.controller.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivationUserDataDTO {

    @NotBlank(message = "encodedUsername cannot be empty")
    private String encodedUsername;

    @NotBlank(message = "tokenValue cannot be empty")
    private String tokenValue;

}