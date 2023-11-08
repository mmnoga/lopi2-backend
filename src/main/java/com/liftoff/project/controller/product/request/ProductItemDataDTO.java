package com.liftoff.project.controller.product.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductItemDataDTO {

    private String productName;
    private String productCategory;
    private Integer productQuantity;
    private Double productPrice;
    private Double productTotalPrice;

}