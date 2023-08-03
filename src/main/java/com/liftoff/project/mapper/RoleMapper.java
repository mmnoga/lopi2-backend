package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.RoleResponseDTO;
import com.liftoff.project.model.Role;



public interface RoleMapper {

    /**
     * Maps Role to RoleResponseDTO
     *
     * @param role
     * @return RoleResponseDTO
     */
    RoleResponseDTO mapRolesToRoleResponses(Role role);

}
