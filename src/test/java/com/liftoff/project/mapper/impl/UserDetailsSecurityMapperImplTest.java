package com.liftoff.project.mapper.impl;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.controller.response.UserResponseDTO;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserDetailsSecurityMapperImplTest {

    @Mock
    private User user;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserDetailsSecurityMapperImpl userDetailsSecurityMapper;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsSecurityMapper = new UserDetailsSecurityMapperImpl();
    }

    @Test
    public void should_Return_Null_Object_For_Null_User() {
        // Given

        User user = null;

        // When
        UserDetailsSecurity mapUserToUserSecurityDetails = userDetailsSecurityMapper.mapUserToUserSecurityDetails(user);

        // Then
        assertNull(mapUserToUserSecurityDetails);
    }

    @Test
    public void should_Return_UserDetailsSecurity_For_Not_Null_User() {
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
        UserDetailsSecurity mapUserToUserSecurityDetails = userDetailsSecurityMapper.mapUserToUserSecurityDetails(user);

        // Then
        assertEquals(user.getFirstName(), mapUserToUserSecurityDetails.getFirstName());
        assertEquals(user.getLastName(), mapUserToUserSecurityDetails.getLastName());
        assertEquals(user.getPassword(), mapUserToUserSecurityDetails.getPassword());
        assertEquals(user.getUuid(), mapUserToUserSecurityDetails.getUuid());
        assertEquals(user.getRoleList().size(), mapUserToUserSecurityDetails.getRoleList().size());




    }




}