package com.liftoff.project.service.impl;

import com.liftoff.project.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CookieServiceImpl.class);

    private final int maxAgeSeconds;

    @Autowired
    public CookieServiceImpl(
            @Value("${cart.cookie.maxAgeSeconds}") int maxAgeSeconds
    ) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    @Override
    public void setCookie(
            String name,
            String value,
            HttpServletResponse response,
            HttpServletRequest request) {

        LOGGER.info("Setting cookie...");

        if (response != null && request != null) {
            String origin = request.getHeader("Origin");

            if (origin != null) {
                String cleanedOrigin = origin.replaceFirst("^https?://", "");
                String domainWithoutPort = cleanedOrigin.split(":")[0];

                ResponseCookie cookie = ResponseCookie.from(name, value)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(maxAgeSeconds)
                        .sameSite("None")
                        .domain(domainWithoutPort)
                        .build();

                response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                LOGGER.info("New cookie: {}", cookie);
            }

        }
    }

    public String getCookieValue(String name, HttpServletRequest request) {

        LOGGER.info("Getting cookie...");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    LOGGER.info("Cookie value: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}