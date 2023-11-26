package com.liftoff.project.controller.payu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BuyerDTO {

    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String language;

}