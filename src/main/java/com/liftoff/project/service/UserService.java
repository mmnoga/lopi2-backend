package com.liftoff.project.service;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.model.User;

public interface UserService {



    public abstract User addUser(SignupRequest signupRequest);
}
