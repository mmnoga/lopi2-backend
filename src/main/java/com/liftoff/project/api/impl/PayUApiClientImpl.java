package com.liftoff.project.api.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PayUApiClientImpl implements PayUApiClient {

    private static final String BASE_URL = "https://secure.snd.payu.com";
    private static final String PAYMENT_METHODS_URL = "/api/v2_1/paymethods";
    private static final String ORDER_CREATE_URL = "/api/v2_1/orders";
    private static final String SHOP_DETAILS_URL = "/api/v2_1/shops";

    @Qualifier("payURestTemplate")
    private final RestTemplate restTemplate;

    @Override
    public PaymentMethodResponseDTO getPaymentMethods() {

        String paymentMethodsUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(PAYMENT_METHODS_URL)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
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
    public OrderResponseDTO submitOrder(OrderCreateRequestDTO orderCreateRequestDTO) {

        HttpHeaders headers = new HttpHeaders();
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
    public ShopDetailsResponseDTO getShopDetails(String shopId) {

        String shopDetailsUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(SHOP_DETAILS_URL + "/{shopId}")
                .buildAndExpand(shopId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ShopDetailsResponseDTO> responseEntity = restTemplate.exchange(
                shopDetailsUrl,
                HttpMethod.GET,
                request,
                ShopDetailsResponseDTO.class);

        return responseEntity.getBody();

    }

    @Override
    public OrderDetailsResponseDTO getOrderDetails(String orderId) {

        String orderDetailsUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(ORDER_CREATE_URL + "/{orderId}")
                .buildAndExpand(orderId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<OrderDetailsResponseDTO> responseEntity = restTemplate.exchange(
                orderDetailsUrl,
                HttpMethod.GET,
                request,
                OrderDetailsResponseDTO.class);

        return responseEntity.getBody();
    }

}