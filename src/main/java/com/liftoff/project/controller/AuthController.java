package com.liftoff.project.controller;

import com.liftoff.project.configuration.jwt.JwtUtils;
import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.exception.CartNotFoundException;
import com.liftoff.project.exception.CookieNotFoundException;
import com.liftoff.project.service.AuthCartService;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.CookieService;
import com.liftoff.project.service.UserService;
import com.liftoff.project.service.UserValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final CartService cartService;
    private final AuthCartService authCartService;
    private final CookieService cookieService;
    private final UserValidationService userValidationService;
    private final JwtUtils jwtUtils;

    @Value("${cart.cookie.name}")
    private String CART_ID_COOKIE_NAME;

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
            @Valid @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletRequest request) {
        JwtResponseDTO jwtResponse = userService.authenticateUser(loginRequestDTO);

        String jwtToken = jwtUtils.generateJwtToken(jwtResponse.getUsername(), jwtResponse.getRole());
        jwtResponse.setToken(jwtToken);

        String authenticatedUsername = jwtResponse.getUsername();

        try {
            mergeCarts(request, authenticatedUsername);
        } catch (CookieNotFoundException ex) {
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private void mergeCarts(HttpServletRequest request, String username) {
        String userCartId = authCartService.findCartIdByUsername(username);

        if (userCartId == null) {
            userCartId = authCartService.createCartForUser(username);
        }

        String unauthenticatedCartId = cookieService.getCookieValue(CART_ID_COOKIE_NAME, request);

        try {
            cartService.mergeCartWithAuthenticatedUser(unauthenticatedCartId, userCartId);
        } catch (CartNotFoundException ex) {
        }
    }

}