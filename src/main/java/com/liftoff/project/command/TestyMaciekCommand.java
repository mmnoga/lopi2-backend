package com.liftoff.project.command;


import com.liftoff.project.model.Role;
import com.liftoff.project.model.RoleName;
import com.liftoff.project.repository.RoleRepository;
import com.liftoff.project.repository.UserRepository;
import jakarta.annotation.Priority;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Priority(3)
@Component
public class TestyMaciekCommand implements CommandLineRunner {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public void run(String... args) throws Exception {


        System.out.println("dupa blada");

        userRepository.findAll()
                .forEach(user -> System.out.println(user.toString()));

      // Role role = roleRepository.findByRoleName("ROLE_USER");
        Role role = roleRepository.findByRoleName(RoleName.ROLE_ADMIN);


        System.out.println("ROLE TERAZ");
     System.out.println(role.getUsers().get(0));

        roleRepository.findAll()
                .forEach(user -> System.out.println(user.toString()));

        System.out.println(RoleName.ROLE_ADMIN);
        System.out.println(RoleName.valueOf("ROLE_USER"));
        System.out.println(RoleName.ROLE_USER.ordinal());



    }

}
