package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.controller.order.response.AddressResponseDTO;
import com.liftoff.project.controller.order.response.CustomerResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.Customer;
import com.liftoff.project.model.order.Order;

public interface OrderMapper {

    OrderSummaryResponseDTO mapOrderToOrderSummaryResponseDTO(Order order);

    OrderDetailsResponseDTO mapOrderToOrderDetailsResponseDTO(Order order);

    AddressResponseDTO mapAddressToAddressResponseDTO(Address address);

    Address mapAddressRequestToAddress(AddressRequestDTO addressRequestDTO);

    CustomerResponseDTO mapCustomerToCustomerResponseDTO (Customer customer);

}
