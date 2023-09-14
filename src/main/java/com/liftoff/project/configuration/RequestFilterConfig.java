package com.liftoff.project.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RequestFilterConfig implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        List<String> allowedMethods = Arrays.asList("GET", "POST", "DELETE", "PUT");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestOrigin = httpRequest.getHeader("Origin");
        String requestMethod = httpRequest.getMethod();

        if (isOriginMatched(requestOrigin, getSupportedOrigins()) &&
                allowedMethods.contains(requestMethod)) {
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", requestOrigin);
            httpResponse.setHeader("Access-Control-Allow-Methods", requestMethod);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getSupportedOrigins() {
        return System.getenv("REQUEST_ORIGINS");
    }

    private boolean isOriginMatched(String origin, String requestOrigin) {
        if (requestOrigin == null || requestOrigin.isEmpty()) {
            return false;
        }
        List<String> allowedOrigins = Arrays.asList(requestOrigin.split(","));
        return allowedOrigins.contains(origin);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}