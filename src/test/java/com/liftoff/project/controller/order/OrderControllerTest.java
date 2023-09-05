package com.liftoff.project.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.controller.order.response.OrderSummaryResponseDTO;
import com.liftoff.project.controller.response.CartItemResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.service.OrderService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Test
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

        LocalDate localDate = LocalDate.of(2023, 9, 5);
        ZoneId zoneId = ZoneId.of("UTC");

        Instant instant = localDate.atStartOfDay(zoneId).toInstant();



        OrderSummaryResponseDTO orderSummaryResponseDTO = OrderSummaryResponseDTO.builder()
                .customerName("ALA")
                //.orderDate(instant)
                .totalPrice(30.30)
                .cartItems(cartItems)
                .build();

        // when
        when(orderService.addOrder(cartUuid)).thenReturn(orderSummaryResponseDTO);

        // then
         mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/add{cartUuid}", cartUuid)
        .contentType(MediaType.APPLICATION_JSON)
                         .content(asJsonString(orderSummaryResponseDTO)))
                 .andExpect(status().isCreated())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Matchers.is("ALA")))
                // .andExpect(MockMvcResultMatchers.jsonPath("$.orderDate", Matchers.is(instant)))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice", Matchers.is(30.30)));
    }


    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}