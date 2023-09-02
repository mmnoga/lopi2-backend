package com.liftoff.project.controller.order.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequestDTO {

    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String postalCode;
    private String city;
    private String country;

}
