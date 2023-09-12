package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.PaymentMethodRequestDTO;
import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import com.liftoff.project.model.order.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentMethodMapperImplTest {


    @InjectMocks
    private PaymentMethodMapperImpl paymentMethodMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void shouldReturnNullPaymentMethodResponseDTOForNullPaymentMethod() {
        // Given
        PaymentMethod paymentMethod = null;
        // When
        PaymentMethodResponseDTO paymentMethodResponseDTO = paymentMethodMapper.mapPaymentMethodToPaymentMethodResponseDTO(paymentMethod);
        // Then
        assertNull(paymentMethodResponseDTO);
    }

    @Test
    void mapPaymentMethodToPaymentMethodResponseDTO() {

        // Given
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(1L)
                .name("ALA")
                .description("KOTA")
                .build();
        // When
        PaymentMethodResponseDTO responseDTO = paymentMethodMapper.mapPaymentMethodToPaymentMethodResponseDTO(paymentMethod);

        // Then
        assertEquals(paymentMethod.getName(), responseDTO.getName());
        assertEquals(paymentMethod.getDescription(), responseDTO.getDescription());

    }

    @Test
    public void shouldReturnNullObjectForNullPaymentMethod() {
        // Given
        PaymentMethodRequestDTO paymentMethodRequestDTO = null;
        // When
        PaymentMethod paymentMethod = paymentMethodMapper.mapPaymentMethodRequestDTOToEntity(paymentMethodRequestDTO);
        // Then
        assertNull(paymentMethod);
    }

    @Test
    void mapPaymentMethodRequestDTOToEntity() {


        // Given
        PaymentMethodRequestDTO paymentMethodRequestDTO = PaymentMethodRequestDTO.builder()
                .name("ALA")
                .description("KOTA")
                .build();

        // When
        PaymentMethod paymentMethod = paymentMethodMapper.mapPaymentMethodRequestDTOToEntity(paymentMethodRequestDTO);

        // Then
        assertEquals(paymentMethod.getName(), paymentMethodRequestDTO.getName());
        assertEquals(paymentMethod.getDescription(), paymentMethodRequestDTO.getDescription());

    }
}