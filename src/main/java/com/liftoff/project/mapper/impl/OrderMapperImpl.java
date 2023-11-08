package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.cart.response.CartItemResponseDTO;
import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.controller.order.request.OrderSummaryDataDTO;
import com.liftoff.project.controller.order.response.AddressResponseDTO;
import com.liftoff.project.controller.order.response.DetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.controller.product.request.ProductItemDataDTO;
import com.liftoff.project.controller.product.response.ProductNameResponseDTO;
import com.liftoff.project.mapper.CartItemMapper;
import com.liftoff.project.mapper.OrderMapper;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final CartItemMapper cartItemMapper;

    @Override
    public OrderSummaryResponseDTO mapOrderToOrderSummaryResponseDTO(Order order) {

        String customerName = "";
        if (order.getCustomer() != null)
            customerName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();

        List<CartItemResponseDTO> cartItems = Collections.emptyList();

        if (order.getCart() != null)
            cartItems = order.getCart().getCartItems().stream()
                    .map(cartItemMapper::mapCartItemToCartItemResponseDTO)
                    .toList();

        return OrderSummaryResponseDTO.builder()
                .customerName(customerName)
                .orderDate(order.getOrderDate())
                .cartItems(cartItems)
                .totalPrice(order.getTotalPrice())
                .build();


    }


    @Override
    public OrderCreatedResponseDTO mapOrderToOrderDetailsResponseDTO(Order order) {

        return OrderCreatedResponseDTO.builder()
                .orderUid(order.getUuid())
                .deliveryMethod(order.getDeliveryMethod().getName())
                .customerEmail(order.getCustomer() != null ? order.getCustomer().getEmail() : "")
                .deliveryAddress(mapAddressToAddressResponseDTO(order.getShippingAddress()))
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDate(order.getOrderDate())
                .customerPhone(order.getCustomer() != null ? order.getCustomer().getPhoneNumber() : "")
                .build();
    }

    @Override
    public OrderDetailsResponseDTO mapOrderToOrderResponseDTO(Order order) {

        return OrderDetailsResponseDTO.builder()
                .orderUid(order.getUuid())
                .deliveryMethod(order.getDeliveryMethod().getName())
                .customerEmail(order.getCustomer() != null ? order.getCustomer().getEmail() : "")
                .deliveryAddress(mapAddressToAddressResponseDTO(order.getShippingAddress()))
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDate(order.getOrderDate())
                .customerPhone(order.getCustomer() != null ? order.getCustomer().getPhoneNumber() : "")
                .productNameResponseDTOS(order.getCart().getCartItems().stream()
                        .map(item -> ProductNameResponseDTO.builder()
                                .productName(item.getProduct().getName())
                                .build())
                        .toList())
                .totalPrice(order.getTotalPrice())
                .deliveryCost(order.getDeliveryCost())
                .build();
    }

    @Override
    public DetailsResponseDTO mapOrderToDetailsResponseDTO(Order order) {

        if (order == null) {
            return null;
        }

        DetailsResponseDTO detailsResponseDTO = DetailsResponseDTO.builder()
                .orderNumber(Optional.ofNullable(order.getUuid())
                        .map(Object::toString)
                        .orElse(""))
                .customerName(Optional.ofNullable(order.getCustomer())
                        .map(Customer::getFullName)
                        .orElse(""))
                .customerPhone(order.getCustomer().getPhoneNumber())
                .customerEmail(order.getCustomer().getEmail())
                .deliveryAddress(AddressResponseDTO.builder()
                        .street(Optional.ofNullable(order.getShippingAddress())
                                .map(Address::getStreet)
                                .orElse(""))
                        .houseNumber(Optional.ofNullable(order.getShippingAddress())
                                .map(Address::getHouseNumber)
                                .orElse(""))
                        .apartmentNumber(Optional.ofNullable(order.getShippingAddress())
                                .map(Address::getApartmentNumber)
                                .orElse(""))
                        .postalCode(Optional.ofNullable(order.getShippingAddress())
                                .map(Address::getPostalCode)
                                .orElse(""))
                        .city(Optional.ofNullable(order.getShippingAddress())
                                .map(Address::getCity)
                                .orElse(""))
                        .country(Optional.ofNullable(order.getShippingAddress())
                                .map(Address::getCountry)
                                .orElse(""))
                        .build()
                )
                .billingAddress(AddressResponseDTO.builder()
                        .street(Optional.ofNullable(order.getBillingAddress())
                                .map(Address::getStreet)
                                .orElse(""))
                        .houseNumber(Optional.ofNullable(order.getBillingAddress())
                                .map(Address::getHouseNumber)
                                .orElse(""))
                        .apartmentNumber(Optional.ofNullable(order.getBillingAddress())
                                .map(Address::getApartmentNumber)
                                .orElse(""))
                        .postalCode(Optional.ofNullable(order.getBillingAddress())
                                .map(Address::getPostalCode)
                                .orElse(""))
                        .city(Optional.ofNullable(order.getBillingAddress())
                                .map(Address::getCity)
                                .orElse(""))
                        .country(Optional.ofNullable(order.getBillingAddress())
                                .map(Address::getCountry)
                                .orElse(""))
                        .build()
                )
                .productList(Optional.ofNullable(order.getCart())
                        .map(Cart::getCartItems)
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(item -> ProductItemDataDTO.builder()
                                .productName(Optional.ofNullable(item.getProduct())
                                        .map(Product::getName)
                                        .orElse(""))
                                .productCategory(getFirstProductCategory(item.getProduct()))
                                .productQuantity(item.getQuantity())
                                .productPrice(Optional.ofNullable(item.getProduct())
                                        .map(Product::getRegularPrice)
                                        .orElse(0.0))
                                .productTotalPrice(item.getSubtotal())
                                .build()
                        )
                        .toList())
                .paymentMethod(Optional.ofNullable(order.getPaymentMethod())
                        .map(PaymentMethod::getName)
                        .orElse(""))
                .deliveryMethod(Optional.ofNullable(order.getDeliveryMethod())
                        .map(DeliveryMethod::getName)
                        .orElse(""))
                .deliveryCost(Optional.ofNullable(order.getDeliveryCost())
                        .orElse(0.0))
                .totalPrice(Optional.ofNullable(order.getTotalPrice())
                        .orElse(0.0))
                .build();

        return detailsResponseDTO;
    }

    @Override
    public OrderSummaryDataDTO mapDetailsResponseDTOToOrderSummaryDataDTO(
            DetailsResponseDTO detailsResponseDTO) {

        if (detailsResponseDTO == null) {
            return null;
        }

        OrderSummaryDataDTO orderSummaryDataDTO = OrderSummaryDataDTO.builder()
                .orderNumber(detailsResponseDTO.getOrderNumber())
                .productList(detailsResponseDTO.getProductList())
                .totalProductsPrice(calculateTotalProductsPrice(detailsResponseDTO.getProductList()))
                .deliveryMethod(detailsResponseDTO.getDeliveryMethod())
                .paymentMethod(detailsResponseDTO.getPaymentMethod())
                .totalPrice(detailsResponseDTO.getTotalPrice())
                .customerName(detailsResponseDTO.getCustomerName())
                .billingStreetAndNumber(buildAddressString(detailsResponseDTO.getBillingAddress()))
                .billingPostalCodeAndCity(buildPostalCodeAndCityString(detailsResponseDTO.getBillingAddress()))
                .phoneNumber(detailsResponseDTO.getCustomerPhone())
                .customerEmail(detailsResponseDTO.getCustomerEmail())
                .deliveryStreetAndNumber(buildAddressString(detailsResponseDTO.getDeliveryAddress()))
                .deliveryPostalCodeAndCity(buildPostalCodeAndCityString(detailsResponseDTO.getDeliveryAddress()))
                .email(detailsResponseDTO.getCustomerEmail())
                .build();

        return orderSummaryDataDTO;
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

    private String getFirstProductCategory(Product product) {
        if (product != null) {
            if (product.getCategories() != null && !product.getCategories().isEmpty()) {
                Category firstCategory = product.getCategories().get(0);
                if (firstCategory != null) {
                    return firstCategory.getName();
                }
            }
        }
        return "";
    }

    private Double calculateTotalProductsPrice(List<ProductItemDataDTO> productList) {
        return productList.stream()
                .map(ProductItemDataDTO::getProductTotalPrice)
                .reduce(0.0, Double::sum);
    }

    private String buildAddressString(AddressResponseDTO address) {
        return address.getStreet() + " " + address.getHouseNumber();
    }

    private String buildPostalCodeAndCityString(AddressResponseDTO address) {
        return address.getPostalCode() + " " + address.getCity();
    }

}