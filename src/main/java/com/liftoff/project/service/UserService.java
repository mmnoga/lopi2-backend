package com.liftoff.project.service;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.controller.auth.request.LoginRequestDTO;
import com.liftoff.project.controller.auth.request.PasswordResetRequestDTO;
import com.liftoff.project.controller.auth.request.ResetLinkRequestDTO;
import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.ActivateUserAccountResponseDTO;
import com.liftoff.project.controller.auth.response.JwtResponseDTO;
import com.liftoff.project.controller.auth.response.PasswordResetResponseDTO;
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

    /**
     * Changes the password for the specified user using a password reset token.
     *
     * @param passwordResetRequestDTO The request containing the user's username, reset token, and new password.
     * @return A {@link PasswordResetResponseDTO} indicating the success of the password change operation.
     * @throws BusinessException if the user is not found, the provided reset token is invalid or has expired.
     */
    PasswordResetResponseDTO changePassword(PasswordResetRequestDTO passwordResetRequestDTO);

    /**
     * Sends a password reset link to the specified user's email address.
     *
     * @param resetLinkRequestDTO The request containing the user's username.
     * @return A {@link PasswordResetResponseDTO} indicating the success of the operation
     * and the email address to which the reset link was sent.
     * @throws BusinessException if the user is not found or if there is an issue sending the email.
     */
    PasswordResetResponseDTO sendPasswordResetLink(ResetLinkRequestDTO resetLinkRequestDTO);

    /**
     * Reactivates a user account using the provided activation data sending new activation link.
     *
     * @param activationUserDataDTO The data required for account reactivation,
     * including an encoded username and a token value.
     * @return An {@link ActivateUserAccountResponseDTO} containing a message indicating
     * the result of the reactivation process.
     * @throws BusinessException If the provided token is valid, a BusinessException is thrown with a message
     * indicating that the token should be used to activate the user account.
     */
    ActivateUserAccountResponseDTO reactivateUserAccount(ActivationUserDataDTO activationUserDataDTO);

}