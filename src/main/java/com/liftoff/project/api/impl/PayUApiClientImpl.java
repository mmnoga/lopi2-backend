package com.liftoff.project.api.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.configuration.payu.PayUConfig;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;
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
    private static final String ORDER_CREATE_URL = "/api/v2_1/orders";
    private static final String SHOP_DETAILS_URL = "/api/v2_1/shops";

    private final RestTemplate restTemplate;
    private final PayUConfig payUConfig;


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
    public OrderResponseDTO submitOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.set("Content-Type", "application/json");

        String submitOrderUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(ORDER_CREATE_URL)
                .build()
                .toUriString();

        HttpEntity<OrderCreateRequestDTO> request =
                new HttpEntity<>(orderCreateRequestDTO, headers);

        ResponseEntity<OrderResponseDTO> responseEntity = restTemplate.exchange(
                submitOrderUrl,
                HttpMethod.POST,
                request,
                OrderResponseDTO.class);

        return responseEntity.getBody();
    }

    @Override
    public ShopDetailsResponseDTO getShopDetails(
            String authorizationHeader,
            String shopId) {

        String shopDetailsUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(SHOP_DETAILS_URL + "/{shopId}")
                .buildAndExpand(shopId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ShopDetailsResponseDTO> responseEntity = restTemplate.exchange(
                shopDetailsUrl,
                HttpMethod.GET,
                request,
                ShopDetailsResponseDTO.class);

        return responseEntity.getBody();

    }

}