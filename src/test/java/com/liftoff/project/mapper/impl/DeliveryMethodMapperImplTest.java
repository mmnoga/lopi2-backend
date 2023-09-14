package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.DeliveryMethodRequestDTO;
import com.liftoff.project.controller.order.response.DeliveryMethodResponseDTO;
import com.liftoff.project.model.order.DeliveryMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DeliveryMethodMapperImplTest {



    @InjectMocks
    private DeliveryMethodMapperImpl deliveryMethodMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnNullDeliveryMethodResponseDTOForNullDeliveryMethod() {
        // Given
        DeliveryMethod deliveryMethod = null;
        // When
        DeliveryMethodResponseDTO deliveryMethodResponseDTO = deliveryMethodMapper.mapDeliveryMethodToDeliveryMethodResponseDTO(deliveryMethod);
        // Then
        assertNull(deliveryMethodResponseDTO);
    }


    @Test
    void mapDeliveryMethodToDeliveryMethodResponseDTO() {

        // Given
        DeliveryMethod deliveryMethod = DeliveryMethod.builder()
                .id(1L)
                .name("ALA")
                .description("KOTA")
                .cost(20.00)
                .build();
        // When
        DeliveryMethodResponseDTO responseDTO = deliveryMethodMapper.mapDeliveryMethodToDeliveryMethodResponseDTO(deliveryMethod);

        // Then
        assertEquals(deliveryMethod.getName(), responseDTO.getName());
        assertEquals(deliveryMethod.getDescription(), responseDTO.getDescription());
        assertEquals(deliveryMethod.getCost(), responseDTO.getCost());
    }

    @Test
    public void shouldReturnNullObjectForNullDeliveryMethod() {
        // Given
        DeliveryMethodRequestDTO deliveryMethodRequestDTO = null;
        // When
        DeliveryMethod  deliveryMethod= deliveryMethodMapper.mapDeliveryMethodRequestDTOToEntity(deliveryMethodRequestDTO);
        // Then
        assertNull(deliveryMethod);
    }


    @Test
    void mapDeliveryMethodRequestDTOToEntity() {

        // Given
        DeliveryMethodRequestDTO deliveryMethodRequestDTO = DeliveryMethodRequestDTO.builder()
                .name("ALA")
                .description("KOTA")
                .cost(20.00)
                .build();

        // When
        DeliveryMethod deliveryMethod = deliveryMethodMapper.mapDeliveryMethodRequestDTOToEntity(deliveryMethodRequestDTO);

        // Then
        assertEquals(deliveryMethod.getName(), deliveryMethodRequestDTO.getName());
        assertEquals(deliveryMethod.getDescription(), deliveryMethodRequestDTO.getDescription());
        assertEquals(deliveryMethod.getDescription(), deliveryMethodRequestDTO.getDescription());


    }
}