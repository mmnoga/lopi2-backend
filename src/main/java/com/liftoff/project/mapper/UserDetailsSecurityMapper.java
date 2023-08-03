package com.liftoff.project.mapper;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.model.User;


public interface UserDetailsSecurityMapper {

    UserDetailsSecurity mapUserToUserSecurityDetails(User user);

}
