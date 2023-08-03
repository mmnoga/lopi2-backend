package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.mapper.RoleMapper;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 class UserMapperImplTest {

    @Mock
    private User user;

    @Mock
    private SignupRequestDTO signupRequestDTO;


    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleMapper roleMapper;


    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserMapperImpl userMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
         userMapper = new UserMapperImpl(roleRepository,roleMapper, passwordEncoder);
    }

    @Test
    public void should_Return_Null_Object_For_Null_User() {
        // Given

        User user = null;

        // When
        UserResponseDTO responseDTO = userMapper.mapUserToUserResponse(user);

        // Then
        assertNull(responseDTO);
    }


    @Test
    public void should_Return_UserResponse_For_Not_Null_User() {
        // Given
        User user = User.builder()
                .withFirstName("Maciej")
                .withLastName("Marciniak")
                .withEmail("genger@wp.pl")
                .withPassword(passwordEncoder.encode("ala ma kota"))
                .withIsEnabled(1)
                .withUuid(UUID.randomUUID())
                .withRoleList(roleRepository.findAll()).build();


        // When
        UserResponseDTO responseDTO = userMapper.mapUserToUserResponse(user);

        // Then
        assertEquals(user.getFirstName(), responseDTO.getFirstName());
        assertEquals(user.getLastName(), responseDTO.getLastName());
        assertEquals(user.getPassword(), responseDTO.getPassword());
        assertEquals(user.getIsEnabled(), responseDTO.getIsEnabled());
        assertEquals(user.getUuid(), responseDTO.getUuid());
        assertEquals(user.getRoleList().size(), responseDTO.getRoleList().size());




    }

    @Test
    public void should_Return_Null_Object_For_Null_ResponseUser() {
        // Given

        SignupRequestDTO signupRequestDTO = null;

        // When
        User user = userMapper.mapSignupRequestToUser(signupRequestDTO);

        // Then
        assertNull(user);
    }


    @Test
    public void should_Return_User_For_Not_Null_UserResponse() {
        // Given

            List<String> roles = new ArrayList<>();
                    roles.add("ROLE_USER");
                    roles.add("ROLE_ADMIN");

        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
                .withFirstName("Maciej")
                .withLastName("Marciniak")
                .withEmail("genger@wp.pl")
                //.withPassword("")
                .withUuid("f97b6441-6ce0-4c95-93b7-9cf9aafa6712")
                .withRoles(roles).build();


        // When
        User user = userMapper.mapSignupRequestToUser(signupRequestDTO);

        user.setUuid(UUID.fromString("f97b6441-6ce0-4c95-93b7-9cf9aafa6712"));

        // Then
        assertEquals(user.getFirstName(), signupRequestDTO.getFirstName());
        assertEquals(user.getLastName(), signupRequestDTO.getLastName());
        assertEquals(user.getPassword(), signupRequestDTO.getPassword());
        assertEquals(user.getUuid().toString(), signupRequestDTO.getUuid());
        assertEquals(user.getRoleList().size(), signupRequestDTO.getRoles().size());




    }


}