package com.liftoff.project.configuration;


import com.liftoff.project.model.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder(setterPrefix = "with")
public class UserDetailsSecurity implements org.springframework.security.core.userdetails.UserDetails {


    private Long id;
    private String email;
    private String username;
    private String password;
    private boolean isEnabled;
    private List<Role> roleList;
    private String firstName;
    private String lastName;
    private UUID uuid;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.getRoleList().stream().map(role->{
          return  new SimpleGrantedAuthority(role.getRoleName().name());
        }).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
