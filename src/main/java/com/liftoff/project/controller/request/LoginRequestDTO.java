package com.liftoff.project.controller.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class LoginRequestDTO {


    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "User name as Email must be valid like name@domain.pl")
    @NotBlank(message = "User's name cannot be empty")
    private String username;

    @NotBlank(message = "User`s password cannot be empty")
    @Size(min = 8, max = 8, message = "Password must be at least 8 characters")
    private String userPass;


}
