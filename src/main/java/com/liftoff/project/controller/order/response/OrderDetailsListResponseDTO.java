package com.liftoff.project.controller.order.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDetailsListResponseDTO {

    private List<OrderDetailsResponseDTO> orderCreatedResponseDTOList;

}
