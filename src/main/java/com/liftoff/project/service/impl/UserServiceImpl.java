package com.liftoff.project.service.impl;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.configuration.jwt.JwtUtils;
import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.controller.auth.request.LoginRequestDTO;
import com.liftoff.project.controller.auth.request.PasswordResetRequestDTO;
import com.liftoff.project.controller.auth.request.ResetLinkRequestDTO;
import com.liftoff.project.controller.auth.request.ResetPasswordDataDTO;
import com.liftoff.project.controller.auth.request.SignupRequestDTO;
import com.liftoff.project.controller.auth.response.ActivateUserAccountResponseDTO;
import com.liftoff.project.controller.auth.response.JwtResponseDTO;
import com.liftoff.project.controller.auth.response.PasswordResetResponseDTO;
import com.liftoff.project.controller.auth.response.UserResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.Token;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.EncoderService;
import com.liftoff.project.service.TokenService;
import com.liftoff.project.service.UserAccountProducerService;
import com.liftoff.project.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UserAccountProducerService userAccountProducerService;
    private final EncoderService encoderService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final int registerTokenExpirationMinutes;
    private final int resetTokenExpirationMinutes;

    public UserServiceImpl(UserRepository userRepository,
                           TokenService tokenService,
                           UserAccountProducerService userAccountProducerService,
                           EncoderService encoderService,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils,
                           @Value("${user.register.token.expiration-minutes}")
                           int registerTokenExpirationMinutes,
                           @Value("${user.passwordReset.token.expiration-minutes}")
                           int resetTokenExpirationMinutes) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userAccountProducerService = userAccountProducerService;
        this.encoderService = encoderService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.registerTokenExpirationMinutes = registerTokenExpirationMinutes;
        this.resetTokenExpirationMinutes = resetTokenExpirationMinutes;
    }

    public UserResponseDTO addUser(SignupRequestDTO signupRequestDTO) {

        User mappedUser = userMapper
                .mapSignupRequestToUser(signupRequestDTO);
        mappedUser.setIsEnabled(false);

        User savedUser = userRepository.save(mappedUser);

        Token token = tokenService.generateTokenForUser(savedUser, registerTokenExpirationMinutes);
        Token savedToken = tokenService.save(token);
        String encodedUsername = encoderService
                .encodeToBase64(savedUser.getUsername());

        ActivationUserDataDTO activationUserDataDTO = ActivationUserDataDTO.builder()
                .encodedUsername(encodedUsername)
                .tokenValue(savedToken.getTokenValue())
                .build();

        userAccountProducerService
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

        validate(token);

        user.setIsEnabled(true);
        tokenService.delete(token);

        User savedActivatedUser = userRepository.save(user);
        String savedUsername = savedActivatedUser.getUsername();

        return ActivateUserAccountResponseDTO.builder()
                .message("User with username: " + savedUsername + " was activated")
                .build();
    }

    @Transactional
    @Override
    public PasswordResetResponseDTO changePassword(
            PasswordResetRequestDTO passwordResetRequestDTO) {

        String encodedUsername = passwordResetRequestDTO.getEncodedUsername();
        String username = encoderService.decodeBase64(encodedUsername);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(
                        "User with username: " + username + " not found",
                        HttpStatus.BAD_REQUEST));

        String tokenValue = passwordResetRequestDTO.getTokenValue();
        Token resetToken = tokenService.getTokenByValue(tokenValue);

        if (resetToken == null || !tokenService.isValid(resetToken)) {
            throw new BusinessException(
                    "Reset token is invalid or has expired",
                    HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordResetRequestDTO.getEncodedPassword();
        String password = encoderService.decodeBase64(encodedPassword);
        user.setPassword(passwordEncoder.encode(password));

        tokenService.delete(resetToken);
        User savedUser = userRepository.save(user);
        String savedUsername = savedUser.getUsername();

        return PasswordResetResponseDTO.builder()
                .message("Password for user: " + savedUsername + " has been changed")
                .build();
    }

    @Override
    public PasswordResetResponseDTO sendPasswordResetLink(
            ResetLinkRequestDTO resetLinkRequestDTO) {

        String username = resetLinkRequestDTO.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BusinessException(
                                "User with username: " + username + " not found",
                                HttpStatus.BAD_REQUEST));

        Token resetToken =
                tokenService.generateTokenForUser(user, resetTokenExpirationMinutes);
        String tokenValue = resetToken.getTokenValue();
        String encodedUsername = encoderService
                .encodeToBase64(username);

        ResetPasswordDataDTO resetPasswordDataDTO = ResetPasswordDataDTO.builder()
                .encodedUsername(encodedUsername)
                .tokenValue(tokenValue)
                .build();

        userAccountProducerService
                .sendResetPasswordDataMessage(resetPasswordDataDTO);

        return PasswordResetResponseDTO.builder()
                .message("Password reset has been initiated for the user: " + username)
                .build();

    }

    @Transactional
    @Override
    public ActivateUserAccountResponseDTO reactivateUserAccount(ActivationUserDataDTO activationUserDataDTO) {

        String encodedUsername = activationUserDataDTO.getEncodedUsername();
        String username = encoderService.decodeBase64(encodedUsername);
        User user = this.getUserByUsername(username);

        String tokenValue = activationUserDataDTO.getTokenValue();
        Token token = tokenService.getTokenByValue(tokenValue);
        if (token.isValid()) {
            throw new BusinessException(
                    "Token is valid. Use it to activate user account",
                    HttpStatus.BAD_REQUEST);
        }
        tokenService.delete(token);

        Token newToken = tokenService.generateTokenForUser(user, registerTokenExpirationMinutes);
        Token savedToken = tokenService.save(newToken);

        activationUserDataDTO.setTokenValue(savedToken.getTokenValue());

        userAccountProducerService
                .sendActivationUserDataMessage(activationUserDataDTO);

        return ActivateUserAccountResponseDTO.builder()
                .message("Password reset has been initiated again for the user: " + user.getUsername())
                .build();
    }

    private void validate(Token token) {
        if (!token.isValid()) {
            throw new BusinessException("Token has expired", HttpStatus.UNAUTHORIZED);
        }
    }

}