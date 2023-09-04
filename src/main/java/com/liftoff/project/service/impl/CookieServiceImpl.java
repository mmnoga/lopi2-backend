package com.liftoff.project.service.impl;

import com.liftoff.project.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CookieServiceImpl implements CookieService {

    private final int maxAgeSeconds;

    @Autowired
    public CookieServiceImpl(
            @Value("${cart.cookie.maxAgeSeconds}") int maxAgeSeconds
    ) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    @Override
    public void setCookie(String name, String value, HttpServletResponse response) {
        if (response != null) {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder
                            .currentRequestAttributes())
                    .getRequest();
            String serverName = request.getServerName();

            String cookieValue = String.format("%s=%s; SameSite=None; Max-Age=%d; Domain=%s; Path=/",
                    name, value, maxAgeSeconds, serverName);

            response.setHeader("Set-Cookie", cookieValue);
            response.setHeader("Access-Control-Allow-Origin", serverName);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    public String getCookieValue(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}