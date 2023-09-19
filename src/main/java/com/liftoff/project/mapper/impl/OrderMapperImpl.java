package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.controller.order.response.AddressResponseDTO;
import com.liftoff.project.controller.order.response.CustomerResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.controller.response.CartItemResponseDTO;
import com.liftoff.project.mapper.CartMapper;
import com.liftoff.project.mapper.DeliveryMethodMapper;
import com.liftoff.project.mapper.OrderMapper;
import com.liftoff.project.mapper.PaymentMethodMapper;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final DeliveryMethodMapper deliveryMethodMapper;
    private final PaymentMethodMapper paymentMethodMapper;

    private static final String PATTERN_FORMAT = "dd.MM.yyyy";


    @Override
    public OrderSummaryResponseDTO mapOrderToOrderSummaryResponseDTO(Order order) {

        String customerName = "";
        if (order.getCustomer() != null)
            customerName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();

        List<CartItemResponseDTO> cartItems = Collections.emptyList();

        if (order.getCart() != null)
            cartItems = order.getCart().getCartItems().stream()
                    .map(cartItem -> cartItemMapper.mapCartItemToCartItemResponseDTO(cartItem))
                    .collect(Collectors.toList());

        OrderSummaryResponseDTO orderSummaryDTO = OrderSummaryResponseDTO.builder()
                .customerName(customerName)
                .orderDate(order.getOrderDate())
                .cartItems(cartItems)
                .totalPrice(order.getTotalPrice())
                .build();

        return orderSummaryDTO;
    }


    @Override
    public OrderDetailsResponseDTO mapOrderToOrderDetailsResponseDTO(Order order) {
//        return OrderDetailsResponseDTO.builder()
//                .uId(order.getUuid())
//                .orderDate(order.getOrderDate())
//                .status(order.getStatus())
//                .totalPrice(order.getTotalPrice())
//                .deliveryMethod(deliveryMethodMapper
//                        .mapDeliveryMethodToDeliveryMethodResponseDTO(order.getDeliveryMethod()))
//                .shippingAddress(mapAddressToAddressResponseDTO(order.getShippingAddress()))
//                .billingAddress(mapAddressToAddressResponseDTO(order.getBillingAddress()))
//                .paymentMethod(paymentMethodMapper.
//                        mapPaymentMethodToPaymentMethodResponseDTO(order.getPaymentMethod()))
//                .cart(cartMapper.mapCartToCartResponseDTO(order.getCart()))
//                .customer(mapCustomerToCustomerResponseDTO(order.getCustomer()))
//                .createdAt(order.getCreatedAt())
//                .updatedAt(order.getUpdatedAt())
//                .termsAccepted(order.getTermsAccepted()!=null?order.getTermsAccepted():false)
//                .build();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT, Locale.ROOT).withZone(ZoneOffset.UTC);


        return OrderDetailsResponseDTO.builder()
                .orderUid(order.getUuid())
                .deliveryMethod(order.getDeliveryMethod().getName())
                .customerEmail(order.getCustomer() !=null? order.getCustomer().getEmail():"")
                .deliveryAddress(mapAddressToAddressResponseDTO(order.getShippingAddress()))
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDate(order.getOrderDate())
                .customerPhone(order.getCustomer() !=null? order.getCustomer().getPhoneNumber():"")
                .build();
    }

    @Override
    public CustomerResponseDTO mapCustomerToCustomerResponseDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponseDTO.builder()
                .customerType(customer.getCustomerType())
                .nip(customer.getNip())
                .companyName(customer.getCompanyName())
                .salutation(customer.getSalutation())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .build();
    }

    @Override
    public AddressResponseDTO mapAddressToAddressResponseDTO(Address address) {
        if (address == null) {
            return null;
        }

        return AddressResponseDTO.builder()
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .apartmentNumber(address.getApartmentNumber())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

    @Override
    public Address mapAddressRequestToAddress(AddressRequestDTO addressRequestDTO) {
        if (addressRequestDTO == null) {
            return null;
        }

        return Address.builder()
                .street(addressRequestDTO.getStreet())
                .houseNumber(addressRequestDTO.getHouseNumber())
                .apartmentNumber(addressRequestDTO.getApartmentNumber())
                .postalCode(addressRequestDTO.getPostalCode())
                .city(addressRequestDTO.getCity())
                .country(addressRequestDTO.getCountry())
                .build();
    }

}