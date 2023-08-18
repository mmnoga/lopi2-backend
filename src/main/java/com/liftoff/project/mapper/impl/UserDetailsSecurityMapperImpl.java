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
                .withId(user.getId())
                .withPassword(user.getPassword())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withIsEnabled(user.getIsEnabled())
                //.withIsEnabled(user.isEnabled)
                .withRole(user.getRole())
                .withUsername(user.getUsername())
                .withUuid(user.getUuid())
                .build();
    }

}
