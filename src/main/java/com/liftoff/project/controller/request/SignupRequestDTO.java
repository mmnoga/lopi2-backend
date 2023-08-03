package com.liftoff.project.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {


    private String uuid;

    @NotBlank
    private String firstName;

    @NotBlank(message = "User's sure name cannot be empty.")
    private String lastName;


    @NotBlank
    @Size(max = 50)
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email must be valid like name@domain.pl")
    private String email;


    @NotBlank
    @Size(min = 5, max = 45)
    private String password;







}
