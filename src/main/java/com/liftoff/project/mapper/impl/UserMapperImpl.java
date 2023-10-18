package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.UserResponseDTO;
import com.liftoff.project.mapper.UserMapper;
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
                .firstName(signupRequestDTO.getFirstName())
                .lastName(signupRequestDTO.getLastName())
                .username(signupRequestDTO.getUsername())
                .phoneNumber(signupRequestDTO.getPhoneNumber())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .uuid(UUID.randomUUID())
                .role(signupRequestDTO.getRole())
                .build();
    }


    public UserResponseDTO mapUserToUserResponse(User user) {

        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .isEnabled(user.getIsEnabled())
                .uuid(user.getUuid())
                .role(user.getRole())
                .build();
    }

}
