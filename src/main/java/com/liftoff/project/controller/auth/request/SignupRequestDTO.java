package com.liftoff.project.controller.auth.request;

import com.liftoff.project.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

    @NotBlank(message = "User's first name cannot be empty.")
    @Size(min = 3, message = "User's first name must be at least 3 characters")
    private String firstName;

    @NotBlank(message = "User's sure name cannot be empty.")
    @Size(min = 3, message = "User's sure name must be at least 3 characters")
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "User name as email must be valid like name@domain.pl")
    @NotBlank(message = "User's name cannot be empty")
    private String username;

    @NotBlank(message = "User's phone number cannot be empty")
    @Size(min = 9, message = "Phone number must be at least 9 characters")
    private String phoneNumber;

    @NotBlank(message = "User`s password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role cannot be empty")
    @Enumerated(EnumType.STRING)
    private Role role;

}
