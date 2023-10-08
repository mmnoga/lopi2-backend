package com.liftoff.project.controller.product.response;

import java.util.List;

public record PaginatedProductResponseDTO(
        List<ProductResponseDTO> products,
        int totalPages,
        long totalProducts,
        boolean hasPrevious,
        boolean hasNext
) {
}