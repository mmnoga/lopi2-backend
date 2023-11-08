package com.liftoff.project.service.impl;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.request.OrderSummaryDataDTO;
import com.liftoff.project.controller.order.request.OrderSummaryForUserRequestDTO;
import com.liftoff.project.controller.order.request.OrderSummaryToSendRequestDTO;
import com.liftoff.project.controller.order.response.DetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryToSendResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.AddressMapper;
import com.liftoff.project.mapper.OrderMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.User;
import com.liftoff.project.model.order.Cost;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.CustomerRepository;
import com.liftoff.project.repository.DeliveryMethodRepository;
import com.liftoff.project.repository.OrderRepository;
import com.liftoff.project.repository.PaymentMethodRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.OrderSummaryProducerService;
import com.liftoff.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final OrderSummaryProducerService orderSummaryProducerService;
    private final UserService userService;
    private static final String DELIVERY_METHOD_NOT_FOUND = "Delivery method not found";
    private static final String ORDER_ENTITY_NOT_FOUND = "Order entity not found";
    private static final String PAYMENT_METHOD_NOT_FOUND = "Payment method not found";

    public OrderCreatedResponseDTO editOrder(OrderRequestDTO orderRequest, UUID orderUuid) {


        Order order = orderRepository.findByUuid(orderUuid)
                .orElseThrow(() ->
                        new BusinessException(ORDER_ENTITY_NOT_FOUND));

        if (orderRequest.getDeliveryMethodName() != null)
            order.setDeliveryMethod(deliveryMethodRepository.findByName(orderRequest.getDeliveryMethodName())
                    .orElseThrow(() ->
                            new BusinessException("DeliveryMethod entity not found")));
        if (orderRequest.getPaymentMethodName() != null)
            order.setPaymentMethod(paymentMethodRepository.findByName(orderRequest.getPaymentMethodName())
                    .orElseThrow(() ->
                            new BusinessException("PaymentMethod entity not found")));
        if (orderRequest.getShippingAddress() != null)
            order.setShippingAddress(addressMapper.mapAddressRequestDTOToAddress(orderRequest.getShippingAddress()));
        if (orderRequest.getBillingAddress() != null)
            order.setBillingAddress(addressMapper.mapAddressRequestDTOToAddress(orderRequest.getBillingAddress()));

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }


    public OrderCreatedResponseDTO changeOrderDeliveryMethod(OrderDeliveryMethodRequestDTO orderChangeRequestDTO, UUID uuid) {

        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() -> new BusinessException(ORDER_ENTITY_NOT_FOUND));

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName(orderChangeRequestDTO.getDeliveryMethodName())
                .orElseThrow(() -> new BusinessException(DELIVERY_METHOD_NOT_FOUND));

        order.setDeliveryMethod(deliveryMethod);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }

    public OrderCreatedResponseDTO changeOrderPaymentMethod(OrderPaymentMethodRequestDTO paymentMethodRequestDTO, UUID uuid) {


        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new BusinessException(ORDER_ENTITY_NOT_FOUND));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName(paymentMethodRequestDTO.getPaymentMethodName())
                .orElseThrow(() ->
                        new BusinessException(PAYMENT_METHOD_NOT_FOUND));

        order.setPaymentMethod(paymentMethod);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }


    public OrderCreatedResponseDTO changeOrderPaymentMethod(String paymentMethod, UUID uuid) {


        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new BusinessException(ORDER_ENTITY_NOT_FOUND));

        PaymentMethod foundPaymentMethod = paymentMethodRepository
                .findByName(paymentMethod)
                .orElseThrow(() ->
                        new BusinessException(PAYMENT_METHOD_NOT_FOUND));

        order.setPaymentMethod(foundPaymentMethod);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderCreatedResponseDTO createOrder(OrderRequestDTO orderRequest, UUID cartUuid) {

        if (Boolean.FALSE.equals(orderRequest.getTermsAccepted())) {
            throw new BusinessException("Terms and conditions were not accepted", HttpStatus.BAD_REQUEST);
        }

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName(orderRequest.getDeliveryMethodName())
                .orElseThrow(() -> new BusinessException(DELIVERY_METHOD_NOT_FOUND));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName(orderRequest.getPaymentMethodName())
                .orElseThrow(() -> new BusinessException(PAYMENT_METHOD_NOT_FOUND));

        Cart cart = cartRepository.findByUuid(cartUuid)
                .orElseThrow(() -> new BusinessException("Cart not found"));
        Customer customer = Customer.builder()
                .customerType(orderRequest.getCustomerType())
                .nip(orderRequest.getNip())
                .companyName(orderRequest.getCompanyName())
                .salutation(orderRequest.getSalutation())
                .firstName(orderRequest.getFirstName())
                .lastName(orderRequest.getLastName())
                .email(orderRequest.getEmail())
                .build();
        Customer newCustomer = customerRepository.save(customer);

        Cost cost = calculateCosts(cart, deliveryMethod);

        Order order = Order.builder()
                .uuid(UUID.randomUUID())
                .customer(newCustomer)
                .orderDate(Instant.now())
                .status(OrderStatus.ACTIVE)
                .totalPrice(cost.total())
                .deliveryMethod(deliveryMethod)
                .shippingAddress(orderMapper.mapAddressRequestToAddress(
                        orderRequest.getShippingAddress()))
                .billingAddress(orderMapper.mapAddressRequestToAddress(
                        orderRequest.getBillingAddress()))
                .paymentMethod(paymentMethod)
                .termsAccepted(true)
                .cart(cart)
                .cartUuid(cartUuid)
                .deliveryCost(deliveryMethod.getCost())
                .build();

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(savedOrder);
    }

    @Override
    @Transactional
    public OrderCreatedResponseDTO createOrderForUser(
            UserDetails userDetails,
            OrderRequestDTO orderRequest,
            UUID cartUuid) {

        User user = userService.getUserByUsername(userDetails.getUsername());

        if (Boolean.FALSE.equals(orderRequest.getTermsAccepted())) {
            throw new BusinessException("Terms and conditions were not accepted", HttpStatus.BAD_REQUEST);
        }

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName(orderRequest.getDeliveryMethodName())
                .orElseThrow(() -> new BusinessException(DELIVERY_METHOD_NOT_FOUND));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName(orderRequest.getPaymentMethodName())
                .orElseThrow(() -> new BusinessException(PAYMENT_METHOD_NOT_FOUND));

        Cart cart = cartRepository.findByUuid(cartUuid)
                .orElseThrow(() -> new BusinessException("Cart not found"));
        Customer customer = Customer.builder()
                .customerType(orderRequest.getCustomerType())
                .nip(orderRequest.getNip())
                .companyName(orderRequest.getCompanyName())
                .salutation(orderRequest.getSalutation())
                .firstName(orderRequest.getFirstName())
                .lastName(orderRequest.getLastName())
                .email(orderRequest.getEmail())
                .build();
        Customer newCustomer = customerRepository.save(customer);

        Cost cost = calculateCosts(cart, deliveryMethod);

        Order order = Order.builder()
                .uuid(UUID.randomUUID())
                .customer(newCustomer)
                .orderDate(Instant.now())
                .status(OrderStatus.ACTIVE)
                .totalPrice(cost.total())
                .deliveryMethod(deliveryMethod)
                .shippingAddress(orderMapper.mapAddressRequestToAddress(
                        orderRequest.getShippingAddress()))
                .billingAddress(orderMapper.mapAddressRequestToAddress(
                        orderRequest.getBillingAddress()))
                .paymentMethod(paymentMethod)
                .termsAccepted(true)
                .cart(cart)
                .cartUuid(cartUuid)
                .deliveryCost(deliveryMethod.getCost())
                .user(user)
                .build();

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(savedOrder);
    }

    @Override
    public OrderSummaryListResponseDTO getAllOrdersSummary() {
        List<Order> orders = orderRepository.findAll();

        return OrderSummaryListResponseDTO.builder()
                .orderSummaryResponseDTOList(orders.stream()
                        .map(orderMapper::mapOrderToOrderSummaryResponseDTO)
                        .toList())
                .build();

    }

    @Override
    public OrderDetailsListResponseDTO getAllOrdersDetails() {
        List<Order> orders = orderRepository.findAll();


        return OrderDetailsListResponseDTO.builder()
                .orderCreatedResponseDTOList(orders.stream()
                        .map(orderMapper::mapOrderToOrderResponseDTO)
                        .toList())
                .build();

    }

    @Override
    public OrderCreatedResponseDTO getOrderByUuid(UUID orderUuid) {
        Order order = orderRepository.findByUuid(orderUuid)
                .orElseThrow(() -> new BusinessException("Order with UUID: " + orderUuid + " not found."));
        return orderMapper.mapOrderToOrderDetailsResponseDTO(order);
    }

    @Override
    public Order getOrderEntityByUuid(UUID orderUuid) {
        return orderRepository
                .findByUuid(orderUuid)
                .orElseThrow(() ->
                        new BusinessException("Order with UUID: " + orderUuid + " not found"));
    }

    @Override
    public DetailsResponseDTO getOrderDetailsByOrderUuid(UUID orderUuid) {

        Order order = getOrderEntityByUuid(orderUuid);

        return orderMapper
                .mapOrderToDetailsResponseDTO(order);
    }

    @Override
    public DetailsResponseDTO getOrderDetailsForUserByOrderUuid(
            UserDetails userDetails,
            UUID orderUuid) {

        Order order = getOrderEntityByUuid(orderUuid);

        User user = userService
                .getUserByUsername(userDetails.getUsername());

        if (!order.getUser().getUsername().equals(user.getUsername())) {
            throw new BusinessException("Order with UUID: " + order.getUuid() +
                    " does not belong to user with UUID: " + user.getUuid());
        }

        return orderMapper
                .mapOrderToDetailsResponseDTO(order);
    }

    @Override
    public OrderSummaryToSendResponseDTO sendOrderSummary(
            OrderSummaryToSendRequestDTO orderSummaryToSendRequestDTO) {

        UUID orderUuid = orderSummaryToSendRequestDTO.getOrderUuid();

        DetailsResponseDTO detailsResponseDTO = getOrderDetailsByOrderUuid(orderUuid);

        OrderSummaryDataDTO orderSummaryDataDTO = orderMapper
                .mapDetailsResponseDTOToOrderSummaryDataDTO(detailsResponseDTO);

        String email = orderSummaryToSendRequestDTO.getEmail();
        orderSummaryDataDTO.setEmail(email);

        orderSummaryProducerService
                .sendOrderSummaryDataMessage(orderSummaryDataDTO);

        return OrderSummaryToSendResponseDTO.builder()
                .message("Order summary send has been initialized")
                .build();
    }

    @Override
    public OrderSummaryToSendResponseDTO sendOrderSummaryForUser(
            UserDetails userDetails,
            OrderSummaryForUserRequestDTO orderSummaryForUserRequestDTO) {

        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        UUID orderUuid = orderSummaryForUserRequestDTO.getOrderUuid();

        DetailsResponseDTO detailsResponseDTO = getOrderDetailsByOrderUuid(orderUuid);

        OrderSummaryDataDTO orderSummaryDataDTO = orderMapper
                .mapDetailsResponseDTOToOrderSummaryDataDTO(detailsResponseDTO);

        String userEmail = user.getUsername();
        orderSummaryDataDTO.setEmail(userEmail);

        orderSummaryProducerService
                .sendOrderSummaryDataMessage(orderSummaryDataDTO);

        return OrderSummaryToSendResponseDTO.builder()
                .message("Order summary send for existing user has been initialized")
                .build();
    }

    private Cost calculateCosts(Cart cart, DeliveryMethod deliveryMethod) {
        Double productsTotalPrice = cart.getCartItems().stream()
                .mapToDouble(item ->
                        item.getProduct().getRegularPrice() * item.getQuantity())
                .sum();

        Double deliveryCost = deliveryMethod != null ? deliveryMethod.getCost() : 0.0;

        Double totalCost = productsTotalPrice + deliveryCost;

        return new Cost(productsTotalPrice, deliveryCost, totalCost);
    }

}