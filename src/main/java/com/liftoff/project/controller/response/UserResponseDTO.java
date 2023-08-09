package com.liftoff.project.controller.response;


import com.liftoff.project.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String firstName;

    private String lastName;

    private String username;

    private boolean isEnabled;

    private UUID uuid;

    private Role role;


}
