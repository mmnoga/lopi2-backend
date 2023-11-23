package com.liftoff.project.controller.payu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestBuyerDTO {


    private String email;
    private String phone;
    private String firstName;
    private String lastName;

}