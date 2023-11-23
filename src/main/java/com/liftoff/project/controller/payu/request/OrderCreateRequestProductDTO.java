package com.liftoff.project.controller.payu.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestProductDTO {


    private String name;
    private String unitPrice;
    private String quantity;


}