package com.liftoff.project.mapper;

import com.liftoff.project.controller.product.request.ProductImageRequestDTO;
import com.liftoff.project.model.ImageAsset;

public interface ImageMapper {

    /**
     * Maps a ProductImageRequestDTO to an ImageAsset entity.
     *
     * @param productImageRequestDTO The ProductImageRequestDTO to be mapped.
     * @return An ImageAsset entity with asset URL populated from the ProductImageRequestDTO,
     * or null if the input is null.
     */
    ImageAsset mapRequestToEntity(ProductImageRequestDTO productImageRequestDTO);

}