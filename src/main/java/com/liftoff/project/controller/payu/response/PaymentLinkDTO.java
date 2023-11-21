package com.liftoff.project.controller.payu.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkDTO {

    private String value;
    private String name;
    private String brandImageUrl;
    private String status;
    private int minAmount;
    private int maxAmount;

}