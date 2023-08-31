package com.liftoff.project.service.impl;

import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.User;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.OrderRepository;
import com.liftoff.project.repository.SessionRepository;
import com.liftoff.project.repository.UserRepository;
import com.liftoff.project.service.CartService;
import com.liftoff.project.service.OrderService;
import com.liftoff.project.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final CartService cartService;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserValidationService userValidationService;
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public void processOrder(UUID cartUuid) {


        User userFromCart = cartService.getCart(cartUuid.toString()).getUser(); // tu też sprawdzam czy jest koszyk

        if (userFromCart != null && this.getUserFromAuthenticatedUser() != null)
            userValidationService.validateAuthenticatedUserFromCart(this.getUserFromAuthenticatedUser(), userFromCart);


        // dodawanie

        Order order = Order.builder()
                .withUuid(UUID.randomUUID())
                .withStatus(OrderStatus.ACTIVE)
                .withTotalPrice(0.00)
                .withOrderDate(LocalDateTime.now())
                .withDeliveryMethod(DeliveryMethod.COURIER_SERVICE)
                .withDeliveryCost(DeliveryMethod.COURIER_SERVICE.getCost())
                .withShippingAddress(null)
                .withPaymentMethod(PaymentMethod.CREDIT_CARD)
                .withBillingAddress(null)
                .build();

        if (userFromCart != null) order.setUser(userFromCart);

        List<CartItem> cartItems = cartService.getCart(cartUuid.toString()).getCartItems();

        List<OrderItem> orderItemList = cartItems.stream().map(cartItem -> OrderItem.builder()
                        .withProduct(cartItem.getProduct())
                        .withQuantity(cartItem.getQuantity())
                        .withUnitPrice(cartItem.getProduct().getRegularPrice())
                        .withSubtotal(cartItem.getQuantity() * cartItem.getProduct().getRegularPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItemList(orderItemList);
        order.setTotalPrice(this.currentTotalPrice(order));
        orderRepository.save(order);

        // to poniżej jeszcze może się przydać
        if (userFromCart != null) { // teraz trzeba sprawdzić czy user nie ma
            // System.out.println(userFromCart.getFirstName());
        } else {
            if (sessionRepository.findByuId(cartUuid).isPresent()) {
                // System.out.println("Mamy koszyk z sesji i możemy działać z nowym zamówieniem");
            }
        }

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


}