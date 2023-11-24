package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.payu.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.payu.response.OrderResponseDTO;
import com.liftoff.project.controller.payu.response.StatusDTO;
import com.liftoff.project.model.OrderPayU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class OrderPayUMapperTest {

    @Mock
    private OrderPayU orderPayU;


    @InjectMocks
    private OrderPayUMapperImpl orderPayUMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldReturnNullObjectForNullOrderPayU() {
        // Given
        OrderPayU orderPayU = null;

        // When
        OrderCreatedResponseDTO orderCreatedResponseDTO = orderPayUMapper.mapOrderToOrderResponseDTO(orderPayU);

        // Then
        assertNull(orderCreatedResponseDTO);
    }

    @Test
    void mapOrderPayUToOrderPayUCreatedResponseDTOTest() {

        // Given
        OrderPayU orderPayU = OrderPayU.builder()
                .id(1L)
                .extOrderId("hhhhh")
                .redirectUri("ala ma kota HTTP")
                .statusCode("WARNING_CONTINUE_3DS")
                .orderId("12345678")
                .build();


        // When
        OrderCreatedResponseDTO dto = orderPayUMapper.mapOrderToOrderResponseDTO(orderPayU);

        // Then
        assertEquals(orderPayU.getExtOrderId(), dto.getExtOrderId());
        assertEquals(orderPayU.getRedirectUri(), dto.getRedirectUri());
        assertEquals(orderPayU.getOrderId(), dto.getOrderId());
        assertEquals(orderPayU.getStatusCode(), dto.getStatus().getStatusCode());

    }


    @Test
    void shouldReturnNullObjectForNullOrderResponseDTO() {
        // Given
        OrderResponseDTO orderResponseDTO = null;

        // When
        OrderPayU orderPayU = orderPayUMapper.mapOrderResponseDTOTOrderPayU(orderResponseDTO);

        // Then
        assertNull(orderPayU);
    }

    // public OrderPayU mapOrderResponseDTOTOrderPayU(OrderResponseDTO orderResponseDTO) {
    @Test
    void mapOrderResponseDTOToOrderPayUTest() {


        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .orderId("12345678")
                .redirectUri("ala ma kota HTTP")
                .extOrderId("hhhhh")
                .status(StatusDTO.builder().statusCode("WARNING_CONTINUE_3DS").build())
                .build();


        // When
        OrderPayU orderPayU = orderPayUMapper.mapOrderResponseDTOTOrderPayU(orderResponseDTO);

        // Then
        assertEquals(orderPayU.getExtOrderId(), orderResponseDTO.getExtOrderId());
        assertEquals(orderPayU.getRedirectUri(), orderResponseDTO.getRedirectUri());
        assertEquals(orderPayU.getOrderId(), orderResponseDTO.getOrderId());
        assertEquals(orderPayU.getStatusCode(), orderResponseDTO.getStatus().getStatusCode());

    }
}
