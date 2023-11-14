package com.liftoff.project.controller.product.request;

import com.liftoff.project.controller.category.request.CategoryUidRequestDTO;
import com.liftoff.project.model.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "name must not be null or empty")
    @Size(min = 1, max = 45, message = "name must be between 1 and 45 characters")
    private String name;
    @Size(min = 1, max = 45, message = "sku must be between 1 and 45 characters")
    private String sku;
    @Positive(message = "regularPrice must be greater than 0")
    private Double regularPrice;
    @Positive(message = "discountPrice must be greater than 0")
    private Double discountPrice;
    private LocalDateTime discountPriceEndDate;
    @Size(min = 5, max = 500, message = "description must be between 5 and 4000 characters")
    private String description;
    @Size(min = 5, max = 500, message = "shortDescription must be between 5 and 500 characters")
    private String shortDescription;
    @Size(min = 5, max = 100, message = "shortDescription must be between 5 and 100 characters")
    private String note;
    private ProductStatus status;
    private String productscol;
    @Positive(message = "quantity must be greater than 0")
    private Integer quantity;
    private List<CategoryUidRequestDTO> categories;
    @Builder.Default
    private List<ProductImageRequestDTO> images = new ArrayList<>();

}
