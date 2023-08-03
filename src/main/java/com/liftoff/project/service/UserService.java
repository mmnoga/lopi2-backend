package com.liftoff.project.service;

import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.model.User;

public interface UserService {



    public abstract User addUser(SignupRequestDTO signupRequestDTO);
    public abstract JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);
}
