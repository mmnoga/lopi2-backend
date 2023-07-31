package com.liftoff.project.mapper;

import com.liftoff.project.controller.request.CategoryRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.model.Category;

public interface CategoryMapper {
    /**
     * Maps the Category entity to the CategoryResponseDTO object.
     *
     * @param category The Category entity to be mapped to the CategoryResponseDTO object.
     * @return The mapped CategoryResponseDTO object.
     */
    CategoryResponseDTO mapEntityToResponse(Category category);

    /**
     * Maps the CategoryRequestDTO object to the Category entity.
     *
     * @param categoryRequestDTO The CategoryRequestDTO object to be mapped to the Category entity.
     * @return The mapped Category entity.
     */
    Category mapRequestToEntity(CategoryRequestDTO categoryRequestDTO);

}
