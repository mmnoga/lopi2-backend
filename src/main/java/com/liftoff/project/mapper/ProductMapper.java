package com.liftoff.project.mapper;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.model.Product;

public interface ProductMapper {
    /**
     * Maps the Product entity to the ProductResponseDTO object.
     *
     * @param product The Product entity to be mapped to the ProductResponseDTO object.
     * @return The mapped ProductResponseDTO object.
     */
    ProductResponseDTO mapEntityToResponse(Product product);

    /**
     * Maps the ProductRequestDTO object to the Product entity.
     *
     * @param productRequestDTO The ProductRequestDTO object to be mapped to the Product entity.
     * @return The mapped Product entity.
     */
    Product mapRequestToEntity(ProductRequestDTO productRequestDTO);

}
