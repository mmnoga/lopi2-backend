package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if (product.getCategories() != null) {
            Set<CategoryResponseDTO> categoryResponses = product.getCategories()
                    .stream()
                    .map(this::mapCategory)
                    .collect(Collectors.toSet());
            response.setCategories(categoryResponses);
        }

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
            Set<CategoryResponseDTO> subcategoryResponses = category.getSubcategories()
                    .stream()
                    .map(this::mapCategory)
                    .collect(Collectors.toSet());
            categoryResponse.getSubcategories().addAll(subcategoryResponses);
        }

        return categoryResponse;
    }

}
