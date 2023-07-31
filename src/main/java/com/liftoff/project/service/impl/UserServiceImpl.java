package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.RoleRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;


    public User addUser(SignupRequest signupRequest) {

        return userRepository.save(userMapper.mapSignupRequestToUser(signupRequest));




    }

}
