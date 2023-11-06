package com.liftoff.project.configuration;

import com.liftoff.project.service.impl.RabbitMQConsumerServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RequestFilterConfig implements Filter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RabbitMQConsumerServiceImpl.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        LOGGER.info("Filtering request");
        List<String> allowedMethods = Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestOrigin = httpRequest.getHeader("Origin");
        String requestMethod = httpRequest.getMethod();

        LOGGER.info("Request filtering requestOrigin {}", requestOrigin);
        LOGGER.info("Request filtering requestMethod {}", requestMethod);

        if (isOriginMatched(requestOrigin, getSupportedOrigins()) &&
                allowedMethods.contains(requestMethod)) {
            LOGGER.info("Adding response headers");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", requestOrigin);
            httpResponse.setHeader("Access-Control-Allow-Methods", String.join(", ", allowedMethods));
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with");
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