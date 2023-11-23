package com.liftoff.project.controller.payu.request;

import java.util.List;

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