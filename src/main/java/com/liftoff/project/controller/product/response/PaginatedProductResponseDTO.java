package com.liftoff.project.controller.product.response;

import com.liftoff.project.controller.product.response.ProductResponseDTO;

import java.util.List;

public record PaginatedProductResponseDTO(
        List<ProductResponseDTO> products,
        int totalPages,

        long totalProducts

) {
}
