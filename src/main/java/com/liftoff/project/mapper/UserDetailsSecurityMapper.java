package com.liftoff.project.mapper;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.model.User;

/**
 * Maps entity User to UserDetailsSecurity
 **/

public interface UserDetailsSecurityMapper {


    /**
     * @param user
     * @return UserDetailsSecurity
     */
    UserDetailsSecurity mapUserToUserSecurityDetails(User user);

}
