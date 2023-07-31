package com.liftoff.project.controller.response;


import com.liftoff.project.model.RoleName;
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
public class RoleResponse {

    private RoleName roleName;

    private UUID uuid;

}
