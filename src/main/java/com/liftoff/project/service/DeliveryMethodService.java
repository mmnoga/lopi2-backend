package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;

import java.util.List;

public interface DeliveryMethodService {

    List<DeliveryMethodResponseDTO> getDeliveryMethods();

    DeliveryMethodResponseDTO addDeliveryMethod(DeliveryMethodRequestDTO deliveryMethodRequestDTO);

    void deleteDeliveryMethodByName(String name);

    DeliveryMethodResponseDTO editDeliveryMethod(String name, DeliveryMethodRequestDTO deliveryMethodRequestDTO);

}
