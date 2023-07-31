package com.liftoff.project.mapper;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.RoleResponse;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.User;

import java.util.List;


public interface RoleMapper {


     List<RoleResponse> mapRolesToRoleResponses(List<Role> roles);

}
