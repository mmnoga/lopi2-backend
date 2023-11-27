package com.liftoff.project.service.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.controller.payu.request.BuyerDTO;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.request.ProductDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.controller.payu.response.ShopDetailsResponseDTO;
import com.liftoff.project.mapper.OrderPayUMapper;
import com.liftoff.project.model.OrderPayU;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.repository.OrderPayURepository;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.PayUService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayUServiceImpl implements PayUService {

    @Value("${payu.shop-id}")
    private String shopId;

    @Value("${payu.merchant-pos-id}")
    private String getMerchantPosId;

    @Value("${payu.notify-url}")
    private String notifyUrl;

    @Value("${payu.continue-url}")
    private String continueUrl;

    @Value("${payu.language}")
    private String buyerLanguage;

    private final PayUApiClient payUApiClient;
    private final OrderPayURepository orderPayURepository;
    private final OrderPayUMapper orderPayUMapper;
    private final OrderService orderService;

    @Override
    public PayUAuthResponseDTO getAccessToken() {
        return payUApiClient
                .getAccessToken();
    }

    @Override
    public PaymentMethodResponseDTO getPaymentMethods(String authorizationHeader) {
        return payUApiClient
                .getPaymentMethods(authorizationHeader);
    }

    @Override
    public OrderCreatedResponseDTO addOrder(
            String authorizationHeader,
            OrderCreateRequestDTO orderCreateRequestDTO) {

        OrderPayU orderPayU = orderPayUMapper
                .mapOrderResponseDTOTOrderPayU(payUApiClient
                        .submitOrder(authorizationHeader, orderCreateRequestDTO));

        orderPayU.setUuid(UUID.randomUUID());

        OrderPayU savedOrderPayU = orderPayURepository
                .save(orderPayU);

        return orderPayUMapper
                .mapOrderToOrderResponseDTO(savedOrderPayU);
    }

    @Override
    public OrderCreatedResponseDTO handlePayment(
            String authorizationHeader,
            UUID orderUuid,
            HttpServletRequest request) {

        Order order = orderService.getOrderEntityByUuid(orderUuid);
        Customer customer = order.getCustomer();

        BuyerDTO buyer = createBuyerDTO(customer);

        List<ProductDTO> products =
                createProductDTOList(order.getOrderItemList());

        String totalPriceForPayU =
                convertToPayUFormat(order.getTotalPrice());

        OrderCreateRequestDTO orderCreateRequestDTO =
                buildOrderCreateRequestDTO(
                        authorizationHeader,
                        buyer,
                        products,
                        totalPriceForPayU,
                        request);

        orderCreateRequestDTO.setExtOrderId(orderUuid.toString());

        OrderResponseDTO orderResponseDTO =
                payUApiClient.submitOrder(authorizationHeader, orderCreateRequestDTO);

        OrderPayU orderPayU = getOrderPayU(orderResponseDTO, order);
        OrderPayU savedOrderPayU = orderPayURepository.save(orderPayU);

        return orderPayUMapper
                .mapOrderToOrderResponseDTO(savedOrderPayU);
    }

    @Override
    public ShopDetailsResponseDTO getShopDetails(
            String authorizationHeader) {

        return payUApiClient
                .getShopDetails(authorizationHeader, shopId);
    }

    private BuyerDTO createBuyerDTO(Customer customer) {

        return BuyerDTO.builder()
                .email(customer.getEmail())
                .phone(customer.getPhoneNumber())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .language(buyerLanguage)
                .build();
    }

    private List<ProductDTO> createProductDTOList(List<OrderItem> orderItems) {

        return orderItems.stream()
                .map(orderItem -> ProductDTO.builder()
                        .name(orderItem.getProduct().getName())
                        .unitPrice(convertToPayUFormat(orderItem.getUnitPrice()))
                        .quantity(orderItem.getQuantity().toString())
                        .build())
                .toList();
    }

    private String convertToPayUFormat(double amount) {

        return String.valueOf((int) (amount * 100));
    }

    private OrderCreateRequestDTO buildOrderCreateRequestDTO(
            String authorizationHeader,
            BuyerDTO buyer,
            List<ProductDTO> products,
            String totalAmount,
            HttpServletRequest request) {

        ShopDetailsResponseDTO shopDetails =
                getShopDetails(authorizationHeader);

        String customerIp = request.getRemoteAddr();

        return OrderCreateRequestDTO.builder()
                .notifyUrl(notifyUrl)
                .customerIp(customerIp)
                .merchantPosId(getMerchantPosId)
                .description(shopDetails.getName())
                .currencyCode(shopDetails.getCurrencyCode())
                .totalAmount(totalAmount)
                .buyer(buyer)
                .products(products)
                .continueUrl(continueUrl)
                .build();
    }

    private OrderPayU getOrderPayU(
            OrderResponseDTO orderResponseDTO,
            Order order) {

        OrderPayU orderPayU = orderPayUMapper
                .mapOrderResponseDTOTOrderPayU(orderResponseDTO);

        orderPayU.setUuid(order.getUuid());

        return orderPayU;
    }

}