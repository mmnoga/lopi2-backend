package com.liftoff.project.controller.auth.response;


import com.liftoff.project.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String phoneNumber;

    private boolean isEnabled;

    private UUID uuid;

    private Role role;


}
