package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.User;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import com.liftoff.project.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderItemMapperTest {


    @Mock
    private Order order;
    @InjectMocks
    private ProductMapperImpl productMapper;

    @InjectMocks
    private OrderItemMapperImpl orderItemMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void shouldReturnNullObjectForNullCategory() {
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
                .withId(1L)
                .build();


        OrderItemRequestDTO orderItemRequestDTO = OrderItemRequestDTO.builder()
                .quantity(10)
                .unitPrice(10.10)
                .subtotal(20.20)
                .build();

        // When
        OrderItem orderItem  = orderItemMapper.mapOrderItemRequestDTOToOrderItem(orderItemRequestDTO,order);

        // Then
        assertEquals(orderItem.getQuantity(), orderItemRequestDTO.getQuantity());
        assertEquals(orderItem.getUnitPrice(), orderItemRequestDTO.getUnitPrice());
        assertEquals(orderItem.getSubtotal(), orderItemRequestDTO.getSubtotal());

    }
}