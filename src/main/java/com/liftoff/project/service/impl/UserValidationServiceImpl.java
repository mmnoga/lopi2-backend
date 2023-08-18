package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.exception.UserExistsException;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.UserValidationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Data
@RequiredArgsConstructor
@Service
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    public void validateUsername(SignupRequestDTO signupRequestDTO) {

        if (userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent())
            throw new UserExistsException("User with username: " + signupRequestDTO.getUsername() + " already exists");
    }

}
