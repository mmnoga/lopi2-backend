package com.liftoff.project.controller.order.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderSummaryListResponseDTO {

    private List<OrderSummaryResponseDTO> orderSummaryResponseDTOList;

}
