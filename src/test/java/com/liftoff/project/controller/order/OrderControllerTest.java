package com.liftoff.project.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.controller.cart.response.CartItemResponseDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.service.OrderService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Disabled
    void shouldCreateOrder() throws Exception {

        // given
        UUID cartUuid = UUID.fromString("7b2b9688-de45-445e-bdf3-a0a7d4ffe733");

        List<CartItemResponseDTO> cartItems = List.of(CartItemResponseDTO.builder()
                .quantity(1)
                .product(ProductResponseDTO.builder()
                        .name("KOT")
                        .regularPrice(30.30)
                        .build())
                .build());


        Instant instant = Instant.now();

        OrderSummaryResponseDTO orderSummaryResponseDTO = OrderSummaryResponseDTO.builder()
                .customerName("ALA")
                .orderDate(instant)
                .totalPrice(30.30)
                .cartItems(cartItems)
                .build();

        // when
        when(orderService.addOrder(cartUuid)).thenReturn(orderSummaryResponseDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/add?cartUuid={cartUuid}", cartUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderSummaryResponseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("ALA"))
                .andExpect(jsonPath("$.orderDate").value(instant.toString()))
                .andExpect(jsonPath("$.totalPrice").value(30.30));
    }


    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}