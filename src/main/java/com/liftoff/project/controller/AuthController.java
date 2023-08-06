package com.liftoff.project.controller;

import com.liftoff.project.configuration.jwt.JwtUtils;
import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserMapper userMapper;



    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequestDTO) {

        //return new ResponseEntity<>(userMapper.mapUserToUserResponse(userService.addUser(signUpRequestDTO)), HttpStatus.CREATED);
        return new ResponseEntity<>(userService.addUser(signUpRequestDTO), HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {


        return new ResponseEntity<>(userService.authenticateUser(loginRequestDTO), HttpStatus.OK);
    }


}