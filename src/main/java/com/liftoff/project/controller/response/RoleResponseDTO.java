package com.liftoff.project.controller.response;


import com.liftoff.project.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {

    private Role roleName;


}
