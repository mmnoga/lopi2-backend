package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.product.request.ProductImageRequestDTO;
import com.liftoff.project.mapper.ImageMapper;
import com.liftoff.project.model.ImageAsset;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl implements ImageMapper {

    @Override
    public ImageAsset mapRequestToEntity(ProductImageRequestDTO productImageRequestDTO) {
        if (productImageRequestDTO == null) {
            return null;
        }

        ImageAsset imageAsset = new ImageAsset();
        imageAsset.setAssetUrl(productImageRequestDTO.getImageUrl());

        return imageAsset;
    }

}