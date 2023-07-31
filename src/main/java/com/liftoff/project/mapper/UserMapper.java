package com.liftoff.project.mapper;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.model.User;


public interface UserMapper {

    User mapSignupRequestToUser(SignupRequest signupRequest);
     UserResponse mapUserToUserResponse(User user);

}
