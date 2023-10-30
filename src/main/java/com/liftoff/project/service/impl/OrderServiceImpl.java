package com.liftoff.project.service.impl;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.AddressMapper;
import com.liftoff.project.mapper.OrderItemMapper;
import com.liftoff.project.mapper.OrderMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.User;
import com.liftoff.project.model.order.Cost;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.CustomerRepository;
import com.liftoff.project.repository.DeliveryMethodRepository;
import com.liftoff.project.repository.OrderRepository;
import com.liftoff.project.repository.PaymentMethodRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final CartService cartService;
    private final UserRepository userRepository;
    private final UserValidationService userValidationService;
    private final OrderRepository orderRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;
    private static final String DELIVERY_METHOD_NOT_FOUND = "Delivery method not found";
    private static final String ORDER_ENTITY_NOT_FOUND = "Order entity not found";
    private static final String PAYMENT_METHOD_NOT_FOUND = "Payment method not found";


    @Override
    @Transactional
    public OrderSummaryResponseDTO addOrder(UUID cartUuid) {


        Cart cart = cartService.getCart(cartUuid.toString());

        User userFromCart = cart.getUser();

        if (orderRepository.findByCartUuid(cartUuid).isPresent())
            throw new BusinessException("This order already exists! Try editing instead of adding");

        if (userFromCart != null && this.getUserFromAuthenticatedUser() != null)
            userValidationService.validateAuthenticatedUserFromCart(this.getUserFromAuthenticatedUser(), userFromCart);

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName("COURIER_SERVICE")
                .orElseThrow(() -> new BusinessException(DELIVERY_METHOD_NOT_FOUND));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName("CREDIT_CARD")
                .orElseThrow(() -> new BusinessException(DELIVERY_METHOD_NOT_FOUND));


        Order order = Order.builder()
                .withUuid(UUID.randomUUID())
                .withStatus(OrderStatus.ACTIVE)
                .withTotalPrice(0.00)
                .withOrderDate(Instant.now())
                .withDeliveryMethod(deliveryMethod)
                .withDeliveryCost(deliveryMethod.getCost())
                .withShippingAddress(null)
                .withPaymentMethod(paymentMethod)
                .withBillingAddress(null)
                .withCartUuid(cart.getUuid())
                .build();

        if (userFromCart != null) order.setUser(userFromCart);

        List<CartItem> cartItems = cart.getCartItems();

        List<OrderItem> orderItemList = cartItems.stream().map(cartItem -> OrderItem.builder()
                        .withProduct(cartItem.getProduct())
                        .withQuantity(cartItem.getQuantity())
                        .withUnitPrice(cartItem.getProduct().getRegularPrice())
                        .withSubtotal(cartItem.getQuantity() * cartItem.getProduct().getRegularPrice())
                        .build())
                .toList();

        order.setOrderItemList(orderItemList);
        order.setTotalPrice(this.currentTotalPrice(order));

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapOrderToOrderSummaryResponseDTO(savedOrder);
    }


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
                .withUuid(UUID.randomUUID())
                .withCustomer(newCustomer)
                .withOrderDate(Instant.now())
                .withStatus(OrderStatus.ACTIVE)
                .withTotalPrice(cost.total())
                .withDeliveryMethod(deliveryMethod)
                .withShippingAddress(orderMapper.mapAddressRequestToAddress(
                        orderRequest.getShippingAddress()))
                .withBillingAddress(orderMapper.mapAddressRequestToAddress(
                        orderRequest.getBillingAddress()))
                .withPaymentMethod(paymentMethod)
                .withTermsAccepted(true)
                .withCart(cart)
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


    private User getUserFromAuthenticatedUser() {

        String userName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) userName = authentication.getName();
        return userRepository.findByUsername(userName).orElse(null);

    }

    private double currentTotalPrice(Order order) {

        double deliveryCost = order.getDeliveryCost();
        double sumOfOrderItemSubTotal = order.getOrderItemList().stream().mapToDouble(OrderItem::getSubtotal).sum();

        return deliveryCost + sumOfOrderItemSubTotal;
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