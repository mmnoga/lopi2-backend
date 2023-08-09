package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.UserValidationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Data
@RequiredArgsConstructor
@Service
public class UserValidationServiceImpl implements UserValidationService {


    private final UserRepository userRepository;

    private Optional<User> getUserExisted(SignupRequestDTO signupRequestDTO) {

        return userRepository.findByUsername(signupRequestDTO.getUsername());

    }


    public String validateUsername(SignupRequestDTO signupRequestDTO) {

        String message = null;

        Optional<User> myOptionalUser = this.getUserExisted(signupRequestDTO);

        if (signupRequestDTO != null) {

            if (myOptionalUser.isPresent()) {

                if (myOptionalUser.get().getUsername().equals(signupRequestDTO.getUsername())) {
                    message = "User with username: " + signupRequestDTO.getUsername() + " already exists";
                }
            }
        }

        return message;
    }

}
