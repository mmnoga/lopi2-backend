package com.liftoff.project.controller.auth.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordDataDTO {

    private String encodedUsername;
    private String tokenValue;

}