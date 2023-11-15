package com.liftoff.project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {

    /**
     * Sets a cookie with the given name and value in the HTTP response.
     *
     * @param name     the name of the cookie
     * @param value    the value to be associated with the cookie
     * @param response the HttpServletResponse object to which the cookie will be added
     * @param request  the HTTP servlet request from which the origin is extracted
     */
    void setCookie(
            String name,
            String value,
            HttpServletResponse response,
            HttpServletRequest request);

    /**
     * Retrieves the value of a cookie with the given name from the HTTP request.
     *
     * @param name     the name of the cookie whose value is to be retrieved
     * @param request  the HttpServletRequest object from which to retrieve the cookie
     * @return         the value associated with the cookie, or null if the cookie is not found
     */
    String getCookieValue(String name, HttpServletRequest request);

}
