package com.liftoff.project.command;

import com.liftoff.project.exception.cart.EntityNotFoundException;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.User;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.AddressRepository;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.DeliveryMethodRepository;
import com.liftoff.project.repository.OrderItemRepository;
import com.liftoff.project.repository.OrderRepository;
import com.liftoff.project.repository.PaymentMethodRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.repository.SessionRepository;
import com.liftoff.project.repository.UserRepository;
import jakarta.annotation.Priority;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@Priority(2)
@AllArgsConstructor
public class OrderInsertCommand implements CommandLineRunner {

    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final SessionRepository sessionRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    @Transactional
    public void run(String... args) {

        Address address1 = Address.builder()
                .phoneNumber("48555-665-888")
                .houseNumber("5A")
                .country("Poland")
                .postalCode("92-009")
                .city("Lodz")
                .street("Krzemieniowa")
                .apartmentNumber("")
                .build();

        Address address2 = Address.builder()
                .phoneNumber("48 111-222-333")
                .houseNumber("510")
                .country("Poland")
                .postalCode("95-112")
                .city("Warsaw")
                .street("Woronicza")
                .apartmentNumber("67")
                .build();

        Address address3 = Address.builder()
                .phoneNumber("48 111-333-444")
                .houseNumber("1")
                .country("Poland")
                .postalCode("00-100")
                .city("Warsaw")
                .street("Rogalskiego")
                .apartmentNumber("2")
                .build();

        Product product1 = new Product();
        Product product2 = new Product();
        if(productRepository.findAll().size()>0) product1 = productRepository.findAll().get(0);
        if(productRepository.findAll().size()>0)  product2 = productRepository.findAll().get(1);

        OrderItem orderItem1 = OrderItem.builder()
                .withQuantity(2)
                .withUnitPrice(87.98)
                .withSubtotal(100.00)
                .build();
        orderItem1.setProduct(product1);


        OrderItem orderItem2 = OrderItem.builder()
                .withQuantity(1)
                .withUnitPrice(12.88)
                .withSubtotal(12.88)
                .build();
        orderItem2.setProduct(product2);

        User user = new User();
        if(userRepository.findAll().size() > 0) user = userRepository.findAll().get(0);

        DeliveryMethod deliveryMethod = deliveryMethodRepository
                .findByName("COURIER_SERVICE")
                .orElseThrow(() -> new EntityNotFoundException("Delivery method not found"));

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName("CREDIT_CARD")
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));

        Order order = Order.builder()
                .withUuid(UUID.randomUUID())
                .withStatus(OrderStatus.ACTIVE)
                .withTotalPrice(10.23)
                .withOrderDate(Instant.now())
                .withDeliveryMethod(deliveryMethod)
                .withDeliveryCost(deliveryMethod.getCost())
                .withShippingAddress(address3)
                .withPaymentMethod(paymentMethod)
                .withBillingAddress(address3)
                .build();


         order.setUser(user);
        order.setOrderItemList(List.of(orderItem1, orderItem2));

        orderRepository.save(order);

        Order order1 = Order.builder()
                .withUuid(UUID.fromString("f9df3191-0641-4c80-a851-24d86a20c26f"))
                .withStatus(OrderStatus.ACTIVE)
                .withTotalPrice(50.23)
                .withOrderDate(Instant.now())
                .withDeliveryMethod(deliveryMethod)
                .withDeliveryCost(deliveryMethod.getCost())
                .withShippingAddress(address3)
                .withPaymentMethod(paymentMethod)
                .withBillingAddress(address3)
                .build();
        order1.setUser(user);
        order1.setOrderItemList(List.of(orderItem1));
        orderRepository.save(order1);

    }

}