package com.liftoff.project.controller;
import com.liftoff.project.configSecurity.UserDetailsSecurity;
import com.liftoff.project.configSecurity.jwt.JwtUtils;
import com.liftoff.project.controller.request.LoginRequest;
import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.JwtResponse;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {



    private AuthenticationManager authenticationManager;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;
    private UserService userService;
    private UserMapper userMapper;


    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        return new ResponseEntity<>(userMapper.mapUserToUserResponse(userService.addUser(signUpRequest)), HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getUserPass()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsSecurity userDetails = (UserDetailsSecurity) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


//        if (userDetails != null && refreshTokenService.countRefreshTokenForUserArch(userDetails.getId()) > 0) {
//            refreshTokenService.deleteByUserId(userDetails.getId());
//        }


       // RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUuid(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getFirstName(),
                userDetails.getLastName()
        ));
    }






}
