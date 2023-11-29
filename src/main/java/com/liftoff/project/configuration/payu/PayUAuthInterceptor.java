package com.liftoff.project.configuration.payu;

import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class PayUAuthInterceptor implements ClientHttpRequestInterceptor {
    private static final String BASE_URL = "https://secure.snd.payu.com";
    private static final String AUTH_URL = "/pl/standard/user/oauth/authorize";
    private final String accessToken;

    public PayUAuthInterceptor(
            @Value("${payu.client-id}") String clientId,
            @Value("${payu.client-secret}") String clientSecret) {
        this.accessToken = getAccessToken(clientId, clientSecret);
    }

    private String getAccessToken(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(AUTH_URL)
                .build()
                .toUriString();

        String requestBody = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s",
                clientId, clientSecret);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PayUAuthResponseDTO> responseEntity = new RestTemplate().exchange(
                authUrl,
                HttpMethod.POST,
                request,
                PayUAuthResponseDTO.class);

        return responseEntity
                .getBody() != null ?
                responseEntity.getBody().getAccessToken() :
                "";
    }

    @Override
    public ClientHttpResponse intercept(
            org.springframework.http.HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return execution.execute(request, body);
    }
}