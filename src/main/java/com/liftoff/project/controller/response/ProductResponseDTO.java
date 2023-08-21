package com.liftoff.project.controller.response;

import com.liftoff.project.model.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ProductResponseDTO {

    private UUID uId;
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
    private Instant createdAt;
    private Instant updatedAt;
    private Instant archivedAt;
    private List<CategoryResponseDTO> categories;
    private List<ImageAssetResponseDTO> imageUrls;

}
