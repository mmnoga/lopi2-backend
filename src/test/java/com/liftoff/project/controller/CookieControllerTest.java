package com.liftoff.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.controller.request.LoginRequestDTO;
import com.liftoff.project.controller.request.SignupRequestDTO;
import com.liftoff.project.controller.response.JwtResponseDTO;
import com.liftoff.project.controller.response.UserResponseDTO;
import com.liftoff.project.mapper.UserMapper;
import com.liftoff.project.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CookieControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    void createCookie() {

        // given
        ResponseCookie resCookie = ResponseCookie.from("some-unauthorized-user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t")
                .httpOnly(true) // js scripts have not an access to our cookie
                .secure(true) // https with ssl protocol is not required
                .path("/api/cookie")
                .maxAge(1 * 24 * 60 * 60) // one day in minutes
                .domain("localhost")
                .build();


        //when


        // then
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/cookie/create")
                            .contentType(MediaType.APPLICATION_JSON))
                           .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.cookie().exists(resCookie.getName()));



        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



}