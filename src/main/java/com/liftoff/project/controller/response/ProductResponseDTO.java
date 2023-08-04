package com.liftoff.project.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
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
        private Boolean published;
        private String productscol;
        private Integer quantity;
        private Instant createdAt;
        private Instant updatedAt;
        private Set<CategoryResponseDTO> categories;
}
