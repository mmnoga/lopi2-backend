package com.liftoff.project.controller.payu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponseDTO {

    @JsonProperty("orders")
    private List<OrderDTO> orders;

    @JsonProperty("status")
    private OrderStatusDTO status;

    @JsonProperty("properties")
    private List<PropertiesDTO> properties;

}