package com.liftoff.project.service;

import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.model.User;

public interface UserValidationService {


     void validateUsername(SignupRequestDTO signupRequestDTO);

     void validateAuthenticatedUserFromCart(User authenticatedUser, User userFromCart);

}
