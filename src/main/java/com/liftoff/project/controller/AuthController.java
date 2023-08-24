package com.liftoff.project.controller;

import com.liftoff.project.configuration.jwt.JwtUtils;
import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.service.UserService;
import com.liftoff.project.service.UserValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Register and authenticate user")
public class AuthController {

    private final UserService userService;
    private final UserValidationService userValidationService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody SignupRequestDTO signUpRequestDTO) {
        userValidationService.validateUsername(signUpRequestDTO);

        return new ResponseEntity<>(
                userService
                        .addUser(signUpRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtResponseDTO jwtResponse = userService.authenticateUser(loginRequestDTO);

        String jwtToken = jwtUtils.generateJwtToken(jwtResponse.getUsername(), jwtResponse.getRole());
        jwtResponse.setToken(jwtToken);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }


}