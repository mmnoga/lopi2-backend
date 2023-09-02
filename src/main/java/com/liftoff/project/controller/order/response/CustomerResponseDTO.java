package com.liftoff.project.controller.order.response;

import com.liftoff.project.model.order.CustomerType;
import com.liftoff.project.model.order.Salutation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponseDTO {

    private CustomerType customerType;
    private String nip;
    private String companyName;
    private Salutation salutation;
    private String firstName;
    private String lastName;
    private String email;

}
