package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.RoleResponseDTO;
import com.liftoff.project.mapper.RoleMapper;
import com.liftoff.project.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapperImpl implements RoleMapper {



    public RoleResponseDTO mapRolesToRoleResponses(Role role){



            return RoleResponseDTO.builder()
                    .withRoleName(role)
                    .build();




    }

}
