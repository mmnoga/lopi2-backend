package com.liftoff.project.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class CookieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateCookieWhenNoCookieExists() throws Exception {

        // given
        // no cookie

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/cookie/create"))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(MockMvcResultMatchers.cookie()
                        .value("some-unauthorized-user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t"));
    }

    @Test
    void shouldDeleteExistingCookie() throws Exception {
        // given
        Cookie existingCookie = new Cookie("some-unauthorized-user-id", "existing-cookie-value");

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/cookie/delete").cookie(existingCookie))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(MockMvcResultMatchers.cookie().value("some-unauthorized-user-id", ""));
    }

    @Test
    void shouldReturnCookieValue() throws Exception {
        // given
        String cookieValue = "test-cookie-value";

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/cookie/read")
                        .cookie(new Cookie("some-unauthorized-user-id", cookieValue)))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Value of the cookie with name some-unauthorized-user-id: " + cookieValue));
    }

    @Test
    void shouldReturnDefaultCookieValueWhenNoCookie() throws Exception {
        // given

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/cookie/read"))

                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Value of the cookie with name some-unauthorized-user-id: default-user-id"));
    }

    @Test
    void shouldReturnAllCookies() throws Exception {
        // given
        Cookie cookie1 = new Cookie("cookie1", "value1");
        Cookie cookie2 = new Cookie("cookie2", "value2");

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/cookie/read/all/cookies")
                        .cookie(cookie1, cookie2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("cookie1 -> value1,cookie2 -> value2"));
    }

    @Test
    void shouldThrowExceptionWhenNoCookies() throws Exception {
        // given

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/cookie/read/all/cookies"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"message\":\"Array of Cookie is empty\"}"));
    }

}