package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.RoleResponseDTO;
import com.liftoff.project.model.Role;

import java.util.List;


public interface RoleMapper {


     List<RoleResponseDTO> mapRolesToRoleResponses(List<Role> roles);

}
