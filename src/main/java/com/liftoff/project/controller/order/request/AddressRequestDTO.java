package com.liftoff.project.controller.order.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 100, message = "Street cannot exceed 100 characters")
    private String street;
    @NotBlank(message = "HouseNumber cannot be blank")
    @Size(max = 10, message = "HouseNumber cannot exceed 10 characters")
    private String houseNumber;
    @Size(max = 10, message = "ApartmentNumber cannot exceed 10 characters")
    private String apartmentNumber;
    @NotBlank(message = "PostalCode cannot be blank")
    @Size(max = 30, message = "PostalCode cannot exceed 30 characters")
    private String postalCode;
    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;
    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;
    @NotBlank(message = "PhoneNumber cannot be blank")
    @Size(max = 30, message = "PhoneNumber cannot exceed 30 characters")
    private String phoneNumber;

}
