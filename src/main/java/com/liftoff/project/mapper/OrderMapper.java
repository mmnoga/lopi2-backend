package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.controller.order.request.OrderSummaryDataDTO;
import com.liftoff.project.controller.order.response.AddressResponseDTO;
import com.liftoff.project.controller.order.response.DetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.Order;

public interface OrderMapper {

    /**
     * Maps the Order entity to the OrderSummaryResponseDTO object.
     *
     * @param order The Order entity to be mapped to the OrderSummaryResponseDTO object.
     * @return The mapped OrderSummaryResponseDTO object.
     */
    OrderSummaryResponseDTO mapOrderToOrderSummaryResponseDTO(Order order);

    /**
     * Maps the Order entity to the OrderDetailsResponseDTO object.
     *
     * @param order The Order entity to be mapped to the OrderDetailsResponseDTO object.
     * @return The mapped OrderDetailsResponseDTO object.
     */
    OrderCreatedResponseDTO mapOrderToOrderDetailsResponseDTO(Order order);

    /**
     * Maps the Order entity to the OrderDetailsResponseDTO object.
     *
     * @param order The Order entity to be mapped to the OrderDetailsResponseDTO object.
     * @return The mapped OrderDetailsResponseDTO object.
     */
    OrderDetailsResponseDTO mapOrderToOrderResponseDTO(Order order);

    /**
     * Maps the Address entity to the AddressResponseDTO object.
     *
     * @param address The Address entity to be mapped to the AddressResponseDTO object.
     * @return The mapped AddressResponseDTO object.
     */
    AddressResponseDTO mapAddressToAddressResponseDTO(Address address);

    /**
     * Maps the AddressRequestDTO object to the Address entity.
     *
     * @param addressRequestDTO The AddressRequestDTO object to be mapped to the Address entity.
     * @return The mapped Address entity.
     */
    Address mapAddressRequestToAddress(AddressRequestDTO addressRequestDTO);

    /**
     * Maps an Order entity to a DetailsResponseDTO for order details presentation.
     *
     * @param order The Order entity to be mapped to the response DTO.
     * @return A DetailsResponseDTO containing the mapped order details,
     * or null if the input order is null.
     */
    DetailsResponseDTO mapOrderToDetailsResponseDTO(Order order);

    /**
     * Maps a DetailsResponseDTO to an OrderSummaryDataDTO for order summary presentation.
     *
     * @param detailsResponseDTO The DetailsResponseDTO to be mapped to the order summary DTO.
     * @return An OrderSummaryDataDTO containing the mapped order summary data,
     * or null if the input details response DTO is null.
     */
    OrderSummaryDataDTO mapDetailsResponseDTOToOrderSummaryDataDTO(
            DetailsResponseDTO detailsResponseDTO);

}