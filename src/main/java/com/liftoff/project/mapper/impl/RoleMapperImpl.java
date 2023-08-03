package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.RoleResponseDTO;
import com.liftoff.project.mapper.RoleMapper;
import com.liftoff.project.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapperImpl implements RoleMapper {



    public List<RoleResponseDTO> mapRolesToRoleResponses(List<Role> roles){


        return   roles.stream().map( (role)->{
            return RoleResponseDTO.builder()
                    .withRoleName(role.getRoleName())
                    .withUuid(role.getUuid())
                    .build();
        }).collect(Collectors.toList());



    }

}
