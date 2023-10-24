package com.liftoff.project.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.controller.order.request.OrderRequestDTO;
import com.liftoff.project.controller.order.response.OrderCreatedResponseDTO;
import com.liftoff.project.controller.order.response.OrderDetailsListResponseDTO;
import com.liftoff.project.controller.order.response.OrderSummaryListResponseDTO;
import com.liftoff.project.model.order.CustomerType;
import com.liftoff.project.model.order.Salutation;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void shouldCreateOrder() throws Exception {


        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .customerType(CustomerType.INDIVIDUAL)
                .salutation(Salutation.MR)
                .firstName("John")
                .lastName("Doe")
                .deliveryMethodName("INPOST")
                .shippingAddress(AddressRequestDTO.builder()
                        .houseNumber("DUPA")
                        .build())
                .billingAddress(AddressRequestDTO.builder().build())
                .paymentMethodName("BLIK")
                .termsAccepted(true)
                .build();
        UUID cartUuid = UUID.randomUUID();

        OrderCreatedResponseDTO expectedResponse = OrderCreatedResponseDTO.builder()
                .paymentMethod("BLIK")
                .deliveryMethod("INPOST")
                .build();

        when(orderService.createOrder(orderRequest, cartUuid))
                .thenReturn(expectedResponse);

        Instant instant = Instant.now();
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderRequest))
                        .param("cartUuid", cartUuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paymentMethod").value("BLIK"))
                .andExpect(jsonPath("$.deliveryMethod").value("INPOST"));
    }

    @Test
    void shouldGetOrderSummary() throws Exception {


        OrderSummaryListResponseDTO orderSummaryListResponseDTO = OrderSummaryListResponseDTO.builder()
                .build();

        when(orderService.getAllOrdersSummary()).thenReturn(orderSummaryListResponseDTO);

        // when/then
        mockMvc.perform(get("/orders/summary")).andExpect(status().isOk());
    }


    @Test
    void shouldGetOrderDetails() throws Exception {


        OrderDetailsListResponseDTO orderDetailsListResponseDTO = OrderDetailsListResponseDTO.builder()
                .build();

        when(orderService.getAllOrdersDetails()).thenReturn(orderDetailsListResponseDTO);

        // when/then
        mockMvc.perform(get("/orders/details")).andExpect(status().isOk());
    }


    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }


}