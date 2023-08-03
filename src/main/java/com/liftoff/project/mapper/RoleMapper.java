package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.RoleResponseDTO;
import com.liftoff.project.model.Role;

import java.util.List;



/**
 * Maps list of RoleResponseDTO To list of Role
 */
public interface RoleMapper {

     /**
      * @param roles = List<Role>
      * @return List<RoleResponseDTO>
      */
     List<RoleResponseDTO> mapRolesToRoleResponses(List<Role> roles);

}
