package com.liftoff.project.controller.order.response;

import com.liftoff.project.controller.order.response.PaymentMethodResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentMethodListResponseDTO {


    private List<PaymentMethodResponseDTO> methodResponseDTOList;

}
