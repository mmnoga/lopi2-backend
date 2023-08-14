package com.liftoff.project.controller;


import com.liftoff.project.exception.CookiesNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cookie")
public class CookieController {


    @GetMapping("/create")
    public ResponseEntity setCookie() {

        ResponseCookie resCookie = ResponseCookie.from("some-unauthorized-user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t").httpOnly(true) // js scripts have not an access to our cookie
                .secure(false) // https with ssl protocol is not required
                .path("/api/cookie") // the cookie will be delivered to the specified URL and all of its subdirectories.
                .maxAge(1 * 24 * 60 * 60) // one day in minutes
                .domain("localhost").build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();

    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteCookie() {

        // create a cookie to overwrite
        ResponseCookie resCookie = ResponseCookie.from("some-unauthorized-user-id", null).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();

    }

    @GetMapping("/read")
    public String readCookie(@CookieValue(name = "some-unauthorized-user-id", defaultValue = "default-user-id") String cookieName) {


        return String.format("Value of the cookie with name some-unauthorized-user-id: %s", cookieName);
    }

    @GetMapping("/read/all/cookies")
    public String readAllCookies(HttpServletRequest request) {


        try {

            return Arrays.stream(request.getCookies()).map((cookie) -> {
                return cookie.getName() + " -> " + cookie.getValue();
            }).collect(Collectors.joining(","));

        } catch (NullPointerException ex) {

            throw new CookiesNotFoundException("Array of Cookie is empty");
        }

    }


}

