package com.liftoff.project.service;

import com.liftoff.project.controller.auth.request.LoginRequestDTO;
import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.JwtResponseDTO;
import com.liftoff.project.controller.auth.response.UserResponseDTO;

public interface UserService {


    /**
     * Adds a new UserResponseDTO based on the provided SignupRequestDTO object
     * and returns the just created UserResponseDTO object.
     *
     * @param signupRequestDTO The SignupRequestDTO object containing the details of the singup request (with new User to add).
     * @return The UserResponseDTO object representing the newly created User entity.
     */
    UserResponseDTO addUser(SignupRequestDTO signupRequestDTO);


    /**
     * login in the User to the application
     * Rreturns JwtResponseDTO based on the provided LoginRequestDTO object after login in the proper User
     *
     * @param loginRequest The LoginRequestDTO object containing the details of the logging in user.
     * @return The JwtResponseDTO object representing the logged user.
     */

    JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);


}
