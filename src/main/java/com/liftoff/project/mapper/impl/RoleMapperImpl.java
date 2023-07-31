package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.RoleResponse;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.mapper.RoleMapper;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.RoleName;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RoleMapperImpl implements RoleMapper {



    public List<RoleResponse> mapRolesToRoleResponses(List<Role> roles){


        return   roles.stream().map( (role)->{
            return RoleResponse.builder()
                    .withRoleName(role.getRoleName())
                    .withUuid(role.getUuid())
                    .build();
        }).collect(Collectors.toList());



    }

}
