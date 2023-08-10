package com.liftoff.project.controller.response;

import java.util.List;

public record PaginatedProductResponseDTO(
        List<ProductResponseDTO> products,
        int totalPages,

        long totalProducts

) {
}
