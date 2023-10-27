package com.liftoff.project.service;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.controller.auth.request.LoginRequestDTO;
import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.ActivateUserAccountResponseDTO;
import com.liftoff.project.controller.auth.response.JwtResponseDTO;
import com.liftoff.project.controller.auth.response.UserResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.User;

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

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The User object if found.
     * @throws BusinessException if the user with the provided username is not found, with a HttpStatus of BAD_REQUEST.
     */
    User getUserByUsername(String username);

    /**
     * Activates a user account based on the provided activation request.
     *
     * @param activateUserRequestDTO The request containing the username and activation token.
     * @return A response DTO indicating the result of the activation.
     * @throws BusinessException If the user or token is not found, or if the token is invalid.
     */
    ActivateUserAccountResponseDTO activateUserAccount(ActivationUserDataDTO activateUserRequestDTO);

}
