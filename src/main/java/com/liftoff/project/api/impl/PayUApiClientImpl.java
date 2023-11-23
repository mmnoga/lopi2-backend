package com.liftoff.project.api.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.configuration.payu.PayUConfig;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderPayUCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.mapper.OrderPayUMapper;
import com.liftoff.project.model.OrderPayU;
import com.liftoff.project.repository.OrderPayURepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PayUApiClientImpl implements PayUApiClient {

    private static final String BASE_URL = "https://secure.snd.payu.com";
    private static final String AUTH_URL = "/pl/standard/user/oauth/authorize";
    private static final String PAYMENT_METHODS_URL = "/api/v2_1/paymethods";
    private static final String ORDER_CRETE_URL = "/api/v2_1/orders";

    private final RestTemplate restTemplate;
    private final PayUConfig payUConfig;
    private final OrderPayURepository orderPayURepository;
    private final OrderPayUMapper orderPayUMapper;

    @Override
    public PayUAuthResponseDTO getAccessToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(AUTH_URL)
                .build()
                .toUriString();

        String requestBody = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s",
                payUConfig.getClientId(), payUConfig.getClientSecret());

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PayUAuthResponseDTO> responseEntity = restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                request,
                PayUAuthResponseDTO.class);

        return responseEntity.getBody();
    }

    @Override
    public PaymentMethodResponseDTO getPaymentMethods(String authorizationHeader) {

        String paymentMethodsUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(PAYMENT_METHODS_URL)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.set("Cache-Control", "no-cache");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<PaymentMethodResponseDTO> responseEntity = restTemplate.exchange(
                paymentMethodsUrl,
                HttpMethod.GET,
                request,
                PaymentMethodResponseDTO.class);

        return responseEntity.getBody();
    }

    @Override
    public OrderPayUCreatedResponseDTO submitOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(ORDER_CRETE_URL)
                .build()
                .toUriString();

        String requestBody = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s",
                payUConfig.getClientId(), payUConfig.getClientSecret());

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OrderResponseDTO> responseEntity = restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                request,
                OrderResponseDTO.class);


        OrderPayU savedOrderPayU = orderPayURepository.save(orderPayUMapper.mapOrderResponseDTOTOrderPayU(responseEntity.getBody()));

       return orderPayUMapper.mapOrderPayUToOrderPayUCreatedResponseDTO(savedOrderPayU);

       // return responseEntity.getBody();
    }

}