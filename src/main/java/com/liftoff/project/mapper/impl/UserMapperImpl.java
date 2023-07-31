package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.mapper.RoleMapper;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.RoleName;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User mapSignupRequestToUser(SignupRequest signupRequest){

        return User.builder()
                .withFirstName(signupRequest.getFirstName())
                .withLastName(signupRequest.getLastName())
                .withEmail(signupRequest.getEmail())
                .withPassword(passwordEncoder.encode(signupRequest.getPassword()))
                .withIsEnabled(0)
                .withUuid(UUID.randomUUID())
                .withRoleList(signupRequest.getRoles().stream().map((role)->{
                    return roleRepository.findByRoleName(RoleName.valueOf(role));
                }).collect(Collectors.toList()))
                .build();
    }

    public UserResponse mapUserToUserResponse(User user){

        return UserResponse.builder()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withIsEnabled(user.getIsEnabled())
                .withUuid(user.getUuid())
                .withRoleList(roleMapper.mapRolesToRoleResponses(user.getRoleList()))
                .build();
    }

}
