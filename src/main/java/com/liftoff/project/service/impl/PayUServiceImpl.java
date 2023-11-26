package com.liftoff.project.service.impl;

import com.liftoff.project.api.PayUApiClient;
import com.liftoff.project.controller.payu.request.BuyerDTO;
import com.liftoff.project.controller.payu.request.OrderCreateRequestDTO;
import com.liftoff.project.controller.payu.request.ProductDTO;
import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.PayUAuthResponseDTO;
import com.liftoff.project.controller.payu.response.PaymentMethodResponseDTO;
import com.liftoff.project.mapper.OrderPayUMapper;
import com.liftoff.project.model.OrderPayU;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.repository.OrderPayURepository;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.PayUService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayUServiceImpl implements PayUService {

    private static final String NOTIFY_URL = "https://your.eshop.com/notify";
    private static final String CUSTOMER_IP = "127.0.0.1";
    private static final String MERCHANT_POS_ID = "470913";
    private static final String DESCRIPTION = "LOPI2 SHOP";
    private static final String CURRENCY_CODE = "PLN";
    private static final String BUYER_LANGUAGE = "pl";

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
            UUID orderUuid) {

        Order order = orderService.getOrderEntityByUuid(orderUuid);
        Customer customer = order.getCustomer();

        BuyerDTO buyer = createBuyerDTO(customer);

        List<ProductDTO> products =
                createProductDTOList(order.getOrderItemList());

        String totalPriceForPayU =
                convertToPayUFormat(order.getTotalPrice());

        OrderCreateRequestDTO orderCreateRequestDTO =
                buildOrderCreateRequestDTO(buyer, products, totalPriceForPayU);

        OrderResponseDTO orderResponseDTO =
                payUApiClient.submitOrder(authorizationHeader, orderCreateRequestDTO);

        OrderPayU orderPayU = getOrderPayU(orderResponseDTO, order);
        OrderPayU savedOrderPayU = orderPayURepository.save(orderPayU);

        return orderPayUMapper
                .mapOrderToOrderResponseDTO(savedOrderPayU);
    }

    private BuyerDTO createBuyerDTO(Customer customer) {

        return BuyerDTO.builder()
                .email(customer.getEmail())
                .phone(customer.getPhoneNumber())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .language(BUYER_LANGUAGE)
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
            BuyerDTO buyer,
            List<ProductDTO> products,
            String totalAmount) {

        return OrderCreateRequestDTO.builder()
                .notifyUrl(NOTIFY_URL)
                .customerIp(CUSTOMER_IP)
                .merchantPosId(MERCHANT_POS_ID)
                .description(DESCRIPTION)
                .currencyCode(CURRENCY_CODE)
                .totalAmount(totalAmount)
                .buyer(buyer)
                .products(products)
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