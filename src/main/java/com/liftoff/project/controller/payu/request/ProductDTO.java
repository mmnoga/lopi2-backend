package com.liftoff.project.controller.payu.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotBlank(message = "name must not be null or empty")
    private String name;

    @NotBlank(message = "quantity must not be null or empty")
    private String unitPrice;

    @NotBlank(message = "quantity must not be null or empty")
    private String quantity;

}