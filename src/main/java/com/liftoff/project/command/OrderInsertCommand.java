package com.liftoff.project.command;

import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.Session;
import com.liftoff.project.model.User;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.model.order.OrderStatus;
import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.AddressRepository;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.OrderItemRepository;
import com.liftoff.project.repository.OrderRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.repository.SessionRepository;
import com.liftoff.project.repository.UserRepository;
import jakarta.annotation.Priority;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Override
    public void run(String... args) {

        Address address1 = Address.builder()
                .withPhoneNumber("48555-665-888")
                .withHouseNumber("5A")
                .withCountry("Poland")
                .withPostalCode("92-009")
                .withCity("Lodz")
                .withStreet("Krzemieniowa")
                .withApartmentNumber("")
                .build();

        Address address2 = Address.builder()
                .withPhoneNumber("48 111-222-333")
                .withHouseNumber("510")
                .withCountry("Poland")
                .withPostalCode("95-112")
                .withCity("Warsaw")
                .withStreet("Woronicza")
                .withApartmentNumber("67")
                .build();

        Address address3 = Address.builder()
                .withPhoneNumber("48 111-333-444")
                .withHouseNumber("1")
                .withCountry("Poland")
                .withPostalCode("00-100")
                .withCity("Warsaw")
                .withStreet("Rogalskiego")
                .withApartmentNumber("2")
                .build();


        Product product1 = productRepository.findAll().get(0);
        Product product2 = productRepository.findAll().get(1);

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

        User user = userRepository.findAll().get(0);

        Order order = Order.builder()
                .withUuid(UUID.randomUUID())
                .withStatus(OrderStatus.ACTIVE)
                .withTotalPrice(10.23)
                .withOrderDate(LocalDateTime.now())
                .withDeliveryMethod(DeliveryMethod.COURIER_SERVICE)
                .withDeliveryCost(DeliveryMethod.COURIER_SERVICE.getCost())
                .withShippingAddress(address3)
                .withPaymentMethod(PaymentMethod.CREDIT_CARD)
                .withBillingAddress(address3)
                .build();


         order.setUser(user);
        order.setOrderItemList(List.of(orderItem1, orderItem2));

        orderRepository.save(order);





    }

}