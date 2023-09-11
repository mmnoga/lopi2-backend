package com.liftoff.project.service;

import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.model.User;

public interface UserValidationService {


    /**
     * Returns, throws UserExistsException when already exists.
     *
     * @param signupRequestDTO The DTO of the new User to validate before add to DB.
     */
    void validateUsername(SignupRequestDTO signupRequestDTO);

    /**
     * Returns, throws BadUserFromCartException when authenticated User try use not his/her cart.
     *
     * @param authenticatedUser The Entity of the User,  Who is authenticated.
     * @param userFromCart      The Entity of the User Who create cart.
     */
    void validateAuthenticatedUserFromCart(User authenticatedUser, User userFromCart);


}
