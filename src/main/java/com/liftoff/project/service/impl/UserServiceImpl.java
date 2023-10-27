package com.liftoff.project.service.impl;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.configuration.jwt.JwtUtils;
import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.controller.auth.request.LoginRequestDTO;
import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.ActivateUserAccountResponseDTO;
import com.liftoff.project.controller.auth.response.JwtResponseDTO;
import com.liftoff.project.controller.auth.response.UserResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.Token;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.EncoderService;
import com.liftoff.project.service.TokenService;
import com.liftoff.project.service.UserActivationProducerService;
import com.liftoff.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UserActivationProducerService userActivationProducerService;
    private final EncoderService encoderService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public UserResponseDTO addUser(SignupRequestDTO signupRequestDTO) {

        User mappedUser = userMapper
                .mapSignupRequestToUser(signupRequestDTO);
        mappedUser.setIsEnabled(false);

        User savedUser = userRepository.save(mappedUser);

        Token token = tokenService.generateTokenForUser(savedUser);
        String encodedUsername = encoderService
                .encodeToBase64(savedUser.getUsername());

        ActivationUserDataDTO activationUserDataDTO = ActivationUserDataDTO.builder()
                .encodedUsername(encodedUsername)
                .tokenValue(token.getTokenValue())
                .build();

        userActivationProducerService
                .sendActivationUserDataMessage(activationUserDataDTO);

        return userMapper.mapUserToUserResponse(savedUser);

    }

    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {


        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsSecurity userDetails = (UserDetailsSecurity) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();


            return JwtResponseDTO.builder()
                    .withToken(jwt)
                    .withUsername(userDetails.getUsername())
                    .withRole(roles.get(0))
                    .withFirstName(userDetails.getFirstName())
                    .withLastName(userDetails.getLastName())
                    .build();


        } catch (AuthenticationException ex) {

            throw new BusinessException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }


    }

    @Override
    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException("User with username: " + username + " not found",
                                HttpStatus.BAD_REQUEST));
    }

    @Override
    public ActivateUserAccountResponseDTO activateUserAccount(
            ActivationUserDataDTO activationUserDataDTO) {

        String encodedUsername = activationUserDataDTO.getEncodedUsername();
        String username = encoderService.decodeBase64(encodedUsername);
        User user = this.getUserByUsername(username);

        String tokenValue = activationUserDataDTO.getTokenValue();
        Token token = tokenService.getTokenByValue(tokenValue);

        validateTokenAndUser(username, token);

        user.setIsEnabled(true);
        tokenService.delete(token);

        User savedActivatedUser = userRepository.save(user);
        String savedUsername = savedActivatedUser.getUsername();

        return ActivateUserAccountResponseDTO.builder()
                .message("User with username: " + savedUsername + " was activated")
                .build();
    }

    private void validateTokenAndUser(String username, Token token) {
        if (!token.getUserUuid().equals(username)) {
            throw new BusinessException("User token does not match the provided username",
                    HttpStatus.BAD_REQUEST);
        }

        if (!token.isValid()) {
            tokenService.delete(token);
            throw new BusinessException("Token has expired", HttpStatus.UNAUTHORIZED);
        }
    }

}