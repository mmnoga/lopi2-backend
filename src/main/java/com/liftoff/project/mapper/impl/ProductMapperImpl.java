package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.category.response.CategoryResponseDTO;
import com.liftoff.project.controller.product.request.ProductRequestDTO;
import com.liftoff.project.controller.product.response.ImageAssetResponseDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponseDTO mapEntityToResponse(Product product) {
        ProductResponseDTO response = ProductResponseDTO.builder()
                .uId(product.getUId())
                .name(product.getName())
                .sku(product.getSku())
                .regularPrice(product.getRegularPrice())
                .discountPrice(product.getDiscountPrice())
                .discountPriceEndDate(product.getDiscountPriceEndDate())
                .lowestPrice(product.getLowestPrice())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .note(product.getNote())
                .status(product.getStatus())
                .productscol(product.getProductscol())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .archivedAt(product.getArchivedAt())
                .build();

        List<ImageAssetResponseDTO> imageAssetResponses = product.getImages()
                .stream()
                .map(imageAsset -> ImageAssetResponseDTO.builder()
                        .imageUrl(imageAsset.getAssetUrl())
                        .build())
                .toList();

        response.setImageUrls(imageAssetResponses);

        List<CategoryResponseDTO> categoryResponses = product.getCategories()
                .stream()
                .map(this::mapCategory)
                .toList();
        response.setCategories(categoryResponses);

        return response;
    }

    @Override
    public Product mapRequestToEntity(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setUId(UUID.randomUUID());
        product.setName(productRequestDTO.getName());
        product.setSku(productRequestDTO.getSku());
        product.setRegularPrice(productRequestDTO.getRegularPrice());
        product.setDiscountPrice(productRequestDTO.getDiscountPrice());
        product.setDiscountPriceEndDate(productRequestDTO.getDiscountPriceEndDate());
        product.setLowestPrice(productRequestDTO.getLowestPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setShortDescription(productRequestDTO.getShortDescription());
        product.setNote(productRequestDTO.getNote());
        product.setStatus(productRequestDTO.getStatus());
        product.setProductscol(productRequestDTO.getProductscol());
        product.setQuantity(productRequestDTO.getQuantity());

        return product;
    }

    private CategoryResponseDTO mapCategory(Category category) {
        CategoryResponseDTO categoryResponse = new CategoryResponseDTO(
                category.getUId(),
                category.getName(),
                category.getDescription());

        if (category.getSubcategories() != null) {
            List<CategoryResponseDTO> subcategoryResponses = category.getSubcategories()
                    .stream()
                    .map(this::mapCategory)
                    .toList();
            categoryResponse.getSubcategories().addAll(subcategoryResponses);
        }

        return categoryResponse;
    }

    private Category mapCategoryResponse(CategoryResponseDTO categoryResponseDTO) {
        Category category = new Category();
        category.setUId(categoryResponseDTO.getUId());
        category.setName(categoryResponseDTO.getName());
        category.setDescription(categoryResponseDTO.getDescription());

        List<Category> subcategories = categoryResponseDTO.getSubcategories()
                .stream()
                .map(this::mapCategoryResponse)
                .toList();
        category.setSubcategories(subcategories);

        return category;
    }

}
