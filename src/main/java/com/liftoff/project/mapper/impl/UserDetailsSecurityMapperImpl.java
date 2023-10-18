package com.liftoff.project.mapper.impl;
import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.mapper.UserDetailsSecurityMapper;
import com.liftoff.project.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsSecurityMapperImpl implements UserDetailsSecurityMapper {


    public UserDetailsSecurity mapUserToUserSecurityDetails(User user) {

        if (user == null) {
            return null;
        }

        return UserDetailsSecurity.builder()
                .id(user.getId())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .isEnabled(user.getIsEnabled())
                //.withIsEnabled(user.isEnabled)
                .role(user.getRole())
                .username(user.getUsername())
                .uuid(user.getUuid())
                .build();
    }

}
