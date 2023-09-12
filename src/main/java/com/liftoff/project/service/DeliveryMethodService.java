package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;

import java.util.List;

public interface DeliveryMethodService {


    /**
     * Returns list of delivery method as DeliveryMethodResponseDTO.
     */
    List<DeliveryMethodResponseDTO> getDeliveryMethods();

    /**
     * Add delivery method from DeliveryMethodRequestDTO.
     *
     * @param deliveryMethodRequestDTO The DTO of the DeliveryMethod.
     */
    DeliveryMethodResponseDTO addDeliveryMethod(DeliveryMethodRequestDTO deliveryMethodRequestDTO);


    /**
     * delete delivery method by name.
     *
     * @param name The String name of the DeliveryMethod.
     */
    void deleteDeliveryMethodByName(String name);

    /**
     * Edit existed delivery method object.
     *
     * @param name                     The String - the name of the DeliveryMethod to be edited.
     * @param deliveryMethodRequestDTO The DTO of the DeliveryMethod to be edited.
     */
    DeliveryMethodResponseDTO editDeliveryMethod(String name, DeliveryMethodRequestDTO deliveryMethodRequestDTO);

}
