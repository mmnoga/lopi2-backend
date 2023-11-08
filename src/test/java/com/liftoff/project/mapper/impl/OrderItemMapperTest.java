package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.controller.product.request.ProductRequestDTO;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderItemMapperTest {


    @Mock
    private Order order;

    @Mock
    private ProductMapperImpl productMapper;

    @InjectMocks
    private OrderItemMapperImpl orderItemMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldReturnNullObjectForNullOrderOrOrderItemRequestDTO() {
        // Given
        Order order = null;
        OrderItemRequestDTO orderItemRequestDTO = null;

        // When
        OrderItem orderItem = orderItemMapper.mapOrderItemRequestDTOToOrderItem(orderItemRequestDTO, order);

        // Then
        assertNull(orderItem);
    }


    @Test
    void mapOrderItemRequestDTOToOrderItem() {

        // Given
        Order order = Order.builder()
                .id(1L)
                .build();


        OrderItemRequestDTO orderItemRequestDTO = OrderItemRequestDTO.builder()
                .quantity(10)
                .unitPrice(10.10)
                .subtotal(20.20)
                .build();

        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .regularPrice(10.00)
                .build();

        orderItemRequestDTO.setProductRequestDTO(productRequestDTO);

        // When
        OrderItem orderItem = orderItemMapper.mapOrderItemRequestDTOToOrderItem(orderItemRequestDTO, order);

        // Then
        assertEquals(orderItem.getQuantity(), orderItemRequestDTO.getQuantity());
        assertEquals(orderItem.getUnitPrice(), orderItemRequestDTO.getUnitPrice());
        assertEquals(orderItem.getSubtotal(), orderItemRequestDTO.getSubtotal());

    }
}