package com.liftoff.project.mapper;

import com.liftoff.project.configSecurity.UserDetailsSecurity;
import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.model.User;


public interface UserDetailsSecurityMapper {

    UserDetailsSecurity mapUserToUserSecurityDetails(User user);

}
