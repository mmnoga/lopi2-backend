package com.liftoff.project.service;

import com.liftoff.project.controller.request.SignupRequestDTO;

public interface UserValidationService {


     String validateUsername(SignupRequestDTO signupRequestDTO);



}
