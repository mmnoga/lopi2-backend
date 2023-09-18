package com.liftoff.project.service.impl;

import com.liftoff.project.controller.order.request.OrderDeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.controller.order.request.OrderPaymentMethodRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.exception.cart.CartNotFoundException;
import com.liftoff.project.exception.cart.EntityNotFoundException;
import com.liftoff.project.exception.cart.TermsNotAcceptedException;
import com.liftoff.project.exception.order.OrderExistsException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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


    @Override
    @Transactional
    public OrderSummaryResponseDTO addOrder(UUID cartUuid) {


        Cart cart = cartService.getCart(cartUuid.toString());

        User userFromCart = cart.getUser();

        if (orderRepository.findByCartUuid(cartUuid).isPresent())
            throw new OrderExistsException("This order already exists! Try editing instead of adding");

        if (userFromCart != null && this.getUserFromAuthenticatedUser() != null)
            userValidationService.validateAuthenticatedUserFromCart(this.getUserFromAuthenticatedUser(), userFromCart);

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName("COURIER_SERVICE")
                .orElseThrow(() -> new EntityNotFoundException("Delivery method not found"));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName("CREDIT_CARD")
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));


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
                .collect(Collectors.toList());

        order.setOrderItemList(orderItemList);
        order.setTotalPrice(this.currentTotalPrice(order));

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapOrderToOrderSummaryResponseDTO(savedOrder);
    }


    public OrderDetailsResponseDTO editOrder(OrderRequestDTO orderRequest, UUID orderUuid) {


        Order order = orderRepository.findByUuid(orderUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order entity not found"));

        if (orderRequest.getDeliveryMethodName() != null)
            order.setDeliveryMethod(deliveryMethodRepository.findByName(orderRequest.getDeliveryMethodName())
                    .orElseThrow(() ->
                            new EntityNotFoundException("DeliveryMethod entity not found")));
        if (orderRequest.getPaymentMethodName() != null)
            order.setPaymentMethod(paymentMethodRepository.findByName(orderRequest.getPaymentMethodName())
                    .orElseThrow(() ->
                            new EntityNotFoundException("PaymentMethod entity not found")));
        if (orderRequest.getShippingAddress() != null)
            order.setShippingAddress(addressMapper.mapAddressRequestDTOToAddress(orderRequest.getShippingAddress()));
        if (orderRequest.getBillingAddress() != null)
            order.setBillingAddress(addressMapper.mapAddressRequestDTOToAddress(orderRequest.getBillingAddress()));


        if (orderRequest.getOrderItemRequestDTOList().size() > 0) orderRequest.getOrderItemRequestDTOList().clear();

        order.setOrderItemList(orderRequest.getOrderItemRequestDTOList().stream()
                .map((OrderItemRequestDTO orderItemRequestDTO) -> {
                    return orderItemMapper.mapOrderItemRequestDTOToOrderItem(orderItemRequestDTO, order);
                })
                .collect(Collectors.toList()));

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }


    public OrderDetailsResponseDTO changeOrderDeliveryMethod(OrderDeliveryMethodRequestDTO orderChangeRequestDTO, UUID uuid) {

        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order entity not found"));

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName(orderChangeRequestDTO.getDeliveryMethodName())
                .orElseThrow(() -> new EntityNotFoundException("Delivery method not found"));

        order.setDeliveryMethod(deliveryMethod);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }

    public OrderDetailsResponseDTO changeOrderPaymentMethod(OrderPaymentMethodRequestDTO paymentMethodRequestDTO, UUID uuid) {


        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order entity not found"));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName(paymentMethodRequestDTO.getPaymentMethodName())
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment method not found"));

        order.setPaymentMethod(paymentMethod);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }

    public OrderDetailsResponseDTO changeOrderPaymentMethod(String paymentMethod, UUID uuid) {


        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new BusinessException("Order entity not found"));

        PaymentMethod foundPaymentMethod = paymentMethodRepository
                .findByName(paymentMethod)
                .orElseThrow(() ->
                        new BusinessException("Payment method not found"));

        order.setPaymentMethod(foundPaymentMethod);

        return orderMapper.mapOrderToOrderDetailsResponseDTO(orderRepository.save(order));
    }

    @Override
    public OrderDetailsResponseDTO createOrder(OrderRequestDTO orderRequest, UUID cartUuid) {
        if (!orderRequest.getTermsAccepted()) {
            throw new TermsNotAcceptedException("Terms and conditions were not accepted");
        }

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName(orderRequest.getDeliveryMethodName())
                .orElseThrow(() -> new EntityNotFoundException("Delivery method not found"));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName(orderRequest.getPaymentMethodName())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));

        Cart cart = cartRepository.findByUuid(cartUuid)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
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
                        .collect(Collectors.toList()))
                .build();
//
//        return orders.stream()
//                .map(orderMapper::mapOrderToOrderSummaryResponseDTO)
//                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailsListResponseDTO getAllOrdersDetails() {
        List<Order> orders = orderRepository.findAll();


        return OrderDetailsListResponseDTO.builder()
                .orderDetailsResponseDTOList(orders.stream()
                        .map(orderMapper::mapOrderToOrderDetailsResponseDTO)
                        .collect(Collectors.toList()))
                .build();
//        return orders.stream()
//                .map(orderMapper::mapOrderToOrderDetailsResponseDTO)
//                .collect(Collectors.toList());
    }

    private User getUserFromAuthenticatedUser() {

        String userName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) userName = authentication.getName();
        return userRepository.findByUsername(userName).orElse(null);

    }

    private double currentTotalPrice(Order order) {

        double deliveryCost = order.getDeliveryCost();
        double sumOfOrderItemSubTotal = order.getOrderItemList().stream().mapToDouble(orderItem -> orderItem.getSubtotal()).sum();

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