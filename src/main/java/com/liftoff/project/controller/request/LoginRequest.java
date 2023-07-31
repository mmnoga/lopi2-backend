package com.liftoff.project.controller.request;




import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {


    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email must be valid like name@domain.pl")
    @NotBlank(message = "User's email cannot be empty.")
    private String username;

    @NotBlank(message = "Hasło użytkownika nie może być puste.")
    private String userPass;




}
