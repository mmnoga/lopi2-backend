package com.liftoff.project.configSecurity;


import com.liftoff.project.mapper.UserDetailsSecurityMapper;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private UserDetailsSecurityMapper userDetailsMapper;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // metoda ta pośredniczy pomiędzy SecurityConfigiem a UserArch Repository

        // todo throw if not exist
        //return this.userArchRepository.findByUserName(username).get();

        Optional<User> myOptionalUser = userRepository.findByEmail(username);

        final UserDetailsSecurity userDetailsSecurity = myOptionalUser.map((user) -> {

            return this.userDetailsMapper.mapUserToUserSecurityDetails(user);

        }).orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });



        return userDetailsSecurity;
    }


}
