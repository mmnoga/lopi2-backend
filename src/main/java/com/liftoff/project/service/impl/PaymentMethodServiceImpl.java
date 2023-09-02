package com.liftoff.project.service.impl;

import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.exception.cart.EntityNotFoundException;
import com.liftoff.project.mapper.PaymentMethodMapper;
import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.PaymentMethodRepository;
import com.liftoff.project.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public List<PaymentMethodResponseDTO> getPaymentMethods() {
        return
                paymentMethodRepository.findAll().stream()
                        .map(paymentMethodMapper::mapPaymentMethodToPaymentMethodResponseDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodResponseDTO addPaymentMethod(PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod paymentMethod = paymentMethodMapper
                .mapPaymentMethodRequestDTOToEntity(paymentMethodRequestDTO);

        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        PaymentMethodResponseDTO paymentMethodResponseDTO = paymentMethodMapper
                .mapPaymentMethodToPaymentMethodResponseDTO(savedPaymentMethod);

        return paymentMethodResponseDTO;
    }

    @Override
    public void deletePaymentMethodByName(String name) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(name)
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment method not found"));
        paymentMethodRepository.delete(paymentMethod);
    }

    @Override
    public PaymentMethodResponseDTO editPaymentMethod(String name, PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findByName(name)
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment method not found"));


        existingPaymentMethod.setName(paymentMethodRequestDTO.getName());
        existingPaymentMethod.setDescription(paymentMethodRequestDTO.getDescription());

        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(existingPaymentMethod);

        return paymentMethodMapper.mapPaymentMethodToPaymentMethodResponseDTO(updatedPaymentMethod);
    }

}