package com.liftoff.project.controller.payu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "customerIp must not be null or empty")
    private String customerIp;

    @NotBlank(message = "merchantPosId must not be null or empty")
    private String merchantPosId;

    @NotBlank(message = "merchantPosId must not be null or empty")
    private String description;

    @NotBlank(message = "currencyCode must not be null or empty")
    private String currencyCode;

    @NotBlank(message = "totalAmount must not be null or empty")
    private String totalAmount;

    private String continueUrl;

    private String extOrderId;

    private BuyerDTO buyer;

    @NotNull(message = "products list must not be null or empty")
    private List<ProductDTO> products;

}