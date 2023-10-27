package com.liftoff.project.service.impl;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.DeliveryMethodMapper;
import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.repository.DeliveryMethodRepository;
import com.liftoff.project.service.DeliveryMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryMethodServiceImpl implements DeliveryMethodService {

    private final DeliveryMethodRepository deliveryMethodRepository;
    private final DeliveryMethodMapper deliveryMethodMapper;

    @Override
    public List<DeliveryMethodResponseDTO> getDeliveryMethods() {
        return
                deliveryMethodRepository.findAll().stream()
                        .map(deliveryMethodMapper::mapDeliveryMethodToDeliveryMethodResponseDTO)
                        .toList();
    }

    @Override
    public DeliveryMethodResponseDTO addDeliveryMethod(DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        DeliveryMethod deliveryMethod = deliveryMethodMapper
                .mapDeliveryMethodRequestDTOToEntity(deliveryMethodRequestDTO);

        DeliveryMethod savedDeliveryMethod = deliveryMethodRepository.save(deliveryMethod);

        return deliveryMethodMapper.mapDeliveryMethodToDeliveryMethodResponseDTO(savedDeliveryMethod);


    }

    @Override
    public void deleteDeliveryMethodByName(String name) {
        DeliveryMethod deliveryMethod = deliveryMethodRepository.findByName(name)
                .orElseThrow(() -> new BusinessException("Delivery method not found"));

        deliveryMethodRepository.delete(deliveryMethod);
    }

    @Override
    public DeliveryMethodResponseDTO editDeliveryMethod(String name, DeliveryMethodRequestDTO deliveryMethodRequestDTO) {
        DeliveryMethod existingDeliveryMethod = deliveryMethodRepository.findByName(name)
                .orElseThrow(() ->
                        new BusinessException("Delivery method not found"));

        existingDeliveryMethod.setName(deliveryMethodRequestDTO.getName());
        existingDeliveryMethod.setDescription(deliveryMethodRequestDTO.getDescription());
        existingDeliveryMethod.setCost(deliveryMethodRequestDTO.getCost());

        DeliveryMethod updatedDeliveryMethod = deliveryMethodRepository.save(existingDeliveryMethod);

        return deliveryMethodMapper.mapDeliveryMethodToDeliveryMethodResponseDTO(updatedDeliveryMethod);
    }

}