package com.liftoff.project.controller;


//import com.example.demo.configSecurity.RefreshTokenService;
//import com.example.demo.configSecurity.UserDetailsSecurity;
//import com.example.demo.configSecurity.jwt.JwtUtils;
//import com.example.demo.exception.BadRequestCustomException;
//import com.example.demo.exception.TokenRefreshException;
//import com.example.demo.exception.advice.ErrorMessage;
//import com.example.demo.mapper.UserArchMapper;
//import com.example.demo.model.dto.request.*;
//import com.example.demo.model.dto.response.JwtResponse;
//import com.example.demo.model.dto.response.TokenRefreshResponse;
//import com.example.demo.model.dto.response.UserArchResponse;
//import com.example.demo.model.entity.RefreshToken;
//import com.example.demo.model.entity.UserArch;
//import com.example.demo.repository.DepartmentRepository;
//import com.example.demo.repository.RoleRepository;
//import com.example.demo.repository.UserArchRepository;
//import com.example.demo.service.UserArchService;
//import com.example.demo.service.validator.UserArchValidationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;


import com.liftoff.project.configSecurity.UserDetailsSecurity;
import com.liftoff.project.configSecurity.jwt.JwtUtils;
import com.liftoff.project.controller.request.LoginRequest;
import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.JwtResponse;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.repository.RoleRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.UserService;
import jakarta.validation.Valid;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
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
