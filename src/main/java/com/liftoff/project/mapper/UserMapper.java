package com.liftoff.project.mapper;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.model.User;

/**
 * Maps entity User on DTO and vice versa
 */

public interface UserMapper {

    /**
     * Maps the SignupRequest to the User entity object.
     * This method is used to change DTO in endpoint on true entity User
     * @param signupRequest
     * @return entity User
     */
    User mapSignupRequestToUser(SignupRequest signupRequest);

    /**
     ** This method is used to prepare to send DTO on endpoint instead of true entity
     ** Maps the User entity to the UserResponse object.
     * @param user
     * @return UserResponse
     */
     UserResponse mapUserToUserResponse(User user);




}
