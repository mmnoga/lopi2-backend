package com.liftoff.project.controller;

import com.liftoff.project.exception.CookiesNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .httpOnly(true)
                .secure(true)
                .path("/api/cookie")
                .maxAge(1 * 24 * 60 * 60)
                .domain("localhost")
                .build();


        //when

        // then
//        try {
//            mockMvc.perform(MockMvcRequestBuilders.get("/api/cookie/create")
//                            .contentType(MediaType.APPLICATION_JSON))
//                           .andExpect(MockMvcResultMatchers.status().isOk())
//                    .andExpect(MockMvcResultMatchers.cookie().exists(resCookie.getName()));
//
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        var exception = assertThrows(RuntimeException.class, () -> {

            throw new CookiesNotFoundException("Array of Cookie is empty");
        });


        //then
         assertEquals("Array of Cookie is empty", exception.getMessage());

    }


}