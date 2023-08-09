package com.liftoff.project.service.impl;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.configuration.jwt.JwtUtils;
import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.exception.LoginAuthenticationException;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public UserResponseDTO addUser(SignupRequestDTO signupRequestDTO) {

        User save = userRepository.save(userMapper.mapSignupRequestToUser(signupRequestDTO));
        return userMapper.mapUserToUserResponse(save);

    }

    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {


        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getUserPass()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsSecurity userDetails = (UserDetailsSecurity) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());


            //        if (userDetails != null && refreshTokenService.countRefreshTokenForUserArch(userDetails.getId()) > 0) {
//            refreshTokenService.deleteByUserId(userDetails.getId());
//        }

            // RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponseDTO(jwt,
                    userDetails.getUsername(),
                    roles.get(0),
                    userDetails.getFirstName(),
                    userDetails.getLastName()
            );
        } catch (Exception ex) { //AuthenticationException
            throw new LoginAuthenticationException(ex.getMessage());
        }


    }


    public User loadUserByUsername(String username) {

        Optional<User> myOptionalUser = userRepository.findByEmail(username);

         final User user = myOptionalUser.orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });


        return user;
    }

}
