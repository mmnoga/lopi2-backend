package com.liftoff.project.controller.product.request;

import com.liftoff.project.controller.category.request.CategoryRequestDTO;
import com.liftoff.project.model.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ProductStatus status;
    private String productscol;
    private Integer quantity;
    private List<CategoryRequestDTO> categories;
    @Builder.Default
    private List<ProductImageRequestDTO> images = new ArrayList<>();

}