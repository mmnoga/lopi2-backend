package com.liftoff.project.configuration.payu;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class ApiConfig {

    @Bean
    @Qualifier("payURestTemplate")
    public RestTemplate restTemplate(
            @Value("${payu.client-id}") String clientId,
            @Value("${payu.client-secret}") String clientSecret) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections
                .singletonList(payUAuthInterceptor(clientId, clientSecret)));

        return restTemplate;
    }

    @Bean
    public PayUAuthInterceptor payUAuthInterceptor(
            @Value("${payu.client-id}") String clientId,
            @Value("${payu.client-secret}") String clientSecret) {

        return new PayUAuthInterceptor(clientId, clientSecret);
    }

}