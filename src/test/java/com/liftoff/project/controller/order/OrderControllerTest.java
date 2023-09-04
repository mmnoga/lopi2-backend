package com.liftoff.project.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.service.OrderService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //@Test
    void shouldCreateOrder() {

        // given
        UUID cartUuid = UUID.fromString("7b2b9688-de45-445e-bdf3-a0a7d4ffe733");


//        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
//                .name("New Product")
//                .description("New Product description")
//                .regularPrice(19.99)
//                .build();
//
//        ProductResponseDTO createdProduct = ProductResponseDTO.builder()
//                .name("New Product")
//                .description("New Product description")
//                .regularPrice(19.99)
//                .build();

        // when
        //when(orderService.addOrder(cartUuid)).thenReturn(null);

        // then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/add{cartUuid}", cartUuid)
//                        .contentType(MediaType.APPLICATION_JSON)
//                       .content("Cart " + cartUuid + " added to Order")
//                       .andExpect(status().isCreated());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("New Product")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("New Product description")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.regularPrice", Matchers.is(19.99)));
    }


    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}