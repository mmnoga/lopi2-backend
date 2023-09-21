package com.liftoff.project.service.impl;
import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.User;
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
            throw new BusinessException("User with username: " + signupRequestDTO.getUsername() + " already exists");
    }


    public void validateAuthenticatedUserFromCart(User authenticatedUser, User userFromCart) {

        if (!userFromCart.getUsername().equals(authenticatedUser.getUsername()) && !authenticatedUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new BusinessException("Authenticated user try use not his/her cart");
        }
    }


}
