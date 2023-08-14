package com.liftoff.project.controller;

import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.service.UserService;
import com.liftoff.project.service.UserValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    private final UserService userService;
    private final UserValidationService userValidationService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequestDTO) {


        userValidationService.validateUsername(signUpRequestDTO);

        return new ResponseEntity<>(userService.addUser(signUpRequestDTO), HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        return new ResponseEntity<>(userService.authenticateUser(loginRequestDTO), HttpStatus.OK);
    }


}