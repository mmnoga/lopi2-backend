package com.liftoff.project.service.impl;

import com.liftoff.project.exception.CookieNotFoundException;
import com.liftoff.project.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CookieServiceImpl implements CookieService {

    private static final int MAX_AGE_SECONDS = 86400;

    @Override
    public void setCookie(String name, String value, HttpServletResponse response) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder
                        .currentRequestAttributes())
                .getRequest();
        String serverName = request.getServerName();

        String cookieValue = String.format("%s=%s; Secure; SameSite=None; Max-Age=%d; Domain=%s; Path=/",
                name, value, MAX_AGE_SECONDS, serverName);

        response.setHeader("Set-Cookie", cookieValue);
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
        throw new CookieNotFoundException("Cookie with name " + name + " not found");
    }

}
