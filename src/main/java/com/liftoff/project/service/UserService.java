package com.liftoff.project.service;

import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.model.User;

public interface UserService {


    /**
     * Adds a new User based on the provided SignupRequestDTO object
     * and returns the just created User object.
     *
     * @param signupRequestDTO The SignupRequestDTO object containing the details of the singup request (with new User to add).
     * @return The User object representing the newly created User entity.
     */
    public abstract User addUser(SignupRequestDTO signupRequestDTO);


    /**
     * login in the User to the application
     * Rreturns JwtResponseDTO based on the provided LoginRequestDTO object after login in the proper User
     *
     * @param loginRequest The LoginRequestDTO object containing the details of the logging in user.
     * @return The JwtResponseDTO object representing the logged user.
     */

    public abstract JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);
}
