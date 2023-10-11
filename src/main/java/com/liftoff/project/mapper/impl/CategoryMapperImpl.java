package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.category.request.CategoryRequestDTO;
import com.liftoff.project.controller.category.request.CategoryUidRequestDTO;
import com.liftoff.project.controller.category.response.CategoryResponseDTO;
import com.liftoff.project.mapper.CategoryMapper;
import com.liftoff.project.model.Category;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponseDTO mapEntityToResponse(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponseDTO.CategoryResponseDTOBuilder builder = CategoryResponseDTO.builder()
                .uId(category.getUId())
                .name(category.getName())
                .description(category.getDescription());

        if (category.containsSubcategories()) {
            Set<CategoryResponseDTO> subcategories = category.getSubcategories().stream()
                    .map(this::mapEntityToResponse)
                    .collect(Collectors.toSet());
            builder.subcategories(subcategories);
        } else {
            builder.subcategories(Collections.emptySet());
        }

        return builder.build();
    }

    @Override
    public Category mapRequestToEntity(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO == null) {
            return null;
        }

        return Category.builder()
                .name(categoryRequestDTO.getName())
                .description(categoryRequestDTO.getDescription())
                .icon(categoryRequestDTO.getIcon())
                .imagePath(categoryRequestDTO.getImagePath())
                .build();
    }

    @Override
    public Category mapUidRequestToEntity(CategoryUidRequestDTO categoryUidRequestDTO) {
        if (categoryUidRequestDTO == null) {
            return null;
        }

        return Category.builder()
                .uId(categoryUidRequestDTO.getCategoryUid())
                .build();
    }

}