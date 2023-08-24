package com.liftoff.project.service;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {

    void setCookie(String name, String value, HttpServletResponse response);

}
