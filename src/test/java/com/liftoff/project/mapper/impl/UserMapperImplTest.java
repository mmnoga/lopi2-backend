package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.request.SignupRequest;
import com.liftoff.project.controller.response.UserResponse;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.RoleName;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.RoleRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 class UserMapperImplTest {


    @Mock
    private User user;

    @Mock
    private SignupRequest signupRequest;


    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserMapperImpl userMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_Return_Null_Object_For_Null_User() {
        // Given

        User user = null;

        // When
        UserResponse responseDTO = userMapper.mapUserToUserResponse(user);

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
                .withPassword("ala ma kota")
                .withIsEnabled(1)
                .withUuid(UUID.fromString("DUPA"))
                .withRoleList(roleRepository.findAll()).build();


        // When
        UserResponse responseDTO = userMapper.mapUserToUserResponse(user);

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

        SignupRequest signupRequest = null;

        // When
        User user = userMapper.mapSignupRequestToUser(signupRequest);

        // Then
        assertNull(user);
    }


    @Test
    public void should_Return_User_For_Not_Null_UserResponse() {
        // Given

            List<String> roles = new ArrayList<>();
                    roles.add("ROLE_USER");
                    roles.add("ROLE_ADMIN");

        SignupRequest signupRequest = SignupRequest.builder()
                .withFirstName("Maciej")
                .withLastName("Marciniak")
                .withEmail("genger@wp.pl")
                .withPassword("ala ma kota")
                .withUuid("DUPA")
                .withRoles(roles).build();


        // When
        User user = userMapper.mapSignupRequestToUser(signupRequest);

        // Then
        assertEquals(user.getFirstName(), signupRequest.getFirstName());
        assertEquals(user.getLastName(), signupRequest.getLastName());
        assertEquals(user.getPassword(), signupRequest.getPassword());
        assertEquals(user.getUuid(), signupRequest.getUuid());
        assertEquals(user.getRoleList().size(), signupRequest.getRoles().size());




    }


}