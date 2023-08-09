package com.liftoff.project.mapper.impl;

import com.liftoff.project.configuration.UserDetailsSecurity;
import com.liftoff.project.model.Role;
import com.liftoff.project.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
class UserDetailsSecurityMapperImplTest {

    @Mock
    private User user;

    @Mock
    private PasswordEncoder passwordEncoder;


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
                .withUsername("genger@wp.pl")
                .withPassword(passwordEncoder.encode("ala ma kota"))
                .withIsEnabled(true)
                .withUuid(UUID.randomUUID())
                .withRole(Role.ROLE_USER)
                .build();


        // When
        UserDetailsSecurity mapUserToUserSecurityDetails = userDetailsSecurityMapper.mapUserToUserSecurityDetails(user);

        // Then
        assertEquals(user.getFirstName(), mapUserToUserSecurityDetails.getFirstName());
        assertEquals(user.getLastName(), mapUserToUserSecurityDetails.getLastName());
        assertEquals(user.getPassword(), mapUserToUserSecurityDetails.getPassword());
        assertEquals(user.getUuid(), mapUserToUserSecurityDetails.getUuid());
        assertEquals(user.getRole(), mapUserToUserSecurityDetails.getRole());


    }


}