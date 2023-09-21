package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.UserResponseDTO;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {


    private final PasswordEncoder passwordEncoder;


    @Override
    public User mapSignupRequestToUser(SignupRequestDTO signupRequestDTO) {

        if (signupRequestDTO == null) {
            return null;
        }

        return User.builder()
                .withFirstName(signupRequestDTO.getFirstName())
                .withLastName(signupRequestDTO.getLastName())
                .withUsername(signupRequestDTO.getUsername())
                .withPassword(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .withIsEnabled(true)
                .withUuid(UUID.randomUUID())
                .withRole(Role.ROLE_USER)
                .build();
    }


    public UserResponseDTO mapUserToUserResponse(User user) {

        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withUsername(user.getUsername())
                .withIsEnabled(user.getIsEnabled())
                .withUuid(user.getUuid())
                .withRole(user.getRole())
                .build();
    }

}
