package com.liftoff.project.controller.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ProductRequestDTO {

    private String name;
    private String sku;
    private Double regularPrice;
    private Double discountPrice;
    private LocalDateTime discountPriceEndDate;
    private Double lowestPrice;
    private String description;
    private String shortDescription;
    private String note;
    private Boolean published;
    private String productscol;
    private Integer quantity;
    private Set<CategoryRequestDTO> categories;

}
