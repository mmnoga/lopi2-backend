package com.liftoff.project.controller.payu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDTO {

    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private String extOrderId;
    private OrderCreateRequestBuyerDTO createRequestBuyerDTO;
    private List<OrderCreateRequestProductDTO> orderCreateRequestProductDTOList;







}