package com.liftoff.project.configuration;


import com.liftoff.project.model.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class UserDetailsSecurity implements org.springframework.security.core.userdetails.UserDetails {


    private Long id;
    private String username;
    private String password;
    private boolean isEnabled;
    private Role role;
    private String firstName;
    private String lastName;
    private UUID uuid;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Role role1 = Optional.ofNullable(this.role)
                .orElse(Role.ROLE_USER);

        return Collections.singleton(new SimpleGrantedAuthority(role1.toString()));


    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

}
