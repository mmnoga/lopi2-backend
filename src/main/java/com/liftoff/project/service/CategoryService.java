package com.liftoff.project.service;

import com.liftoff.project.controller.request.CategoryRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.exception.category.CategoryNotFoundException;
import com.liftoff.project.exception.category.ParentCategoryNotFoundException;
import com.liftoff.project.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    /**
     * Retrieves a list of all categories as CategoryResponseDTO object.
     *
     * @return The list of CategoryResponseDTO objects representing all categories.
     */
    List<CategoryResponseDTO> getAllCategories();

    /**
     * Retrieves a specific category by its UUID as the CategoryResponseDTO object.
     *
     * @param categoryUuId The UUID of the category to be retrieved.
     * @return The CategoryResponseDTO object representing the category with the specified UUID.
     * @throws CategoryNotFoundException If the category with the given UUID is not found.
     */
    CategoryResponseDTO getCategoryByUuId(UUID categoryUuId);

    /**
     * Adds a new category based on the provided CategoryRequestDTO object
     * and returns the created category as the CategoryResponseDTO object.
     *
     * @param categoryRequestDTO The CategoryRequestDTO object containing the details of the category to be added.
     * @return The CategoryResponseDTO object representing the newly created category.
     */
    CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO);

    /**
     * Updates an existing category with the specified UUID based on the information provided
     * in the CategoryRequestDTO object.
     *
     * @param categoryUuid The UUID of the category to be updated.
     * @param categoryRequestDTO The CategoryRequestDTO object containing the updated details of the category.
     * @return The CategoryResponseDTO object representing the updated category.
     * @throws CategoryNotFoundException If the category with the given UUID is not found.
     */
    CategoryResponseDTO updateCategory(UUID categoryUuid, CategoryRequestDTO categoryRequestDTO);

    /**
     * Deletes a category with the specified UUID.
     *
     * @param categoryUuid The UUID of the category to be deleted.
     * @throws CategoryNotFoundException If the category with the given UUID is not found.
     */
    void deleteCategoryByUuid(UUID categoryUuid);

    /**
     * Retrieves a list of existing categories based on the provided list of CategoryRequestDTO objects.
     *
     * @param categories The set of CategoryRequestDTO object representing categories to be checked.
     * @return A set of existing Category object corresponding to the provided CategoryRequestDTO objects.
     * @throws ParentCategoryNotFoundException If a parent category specified
     * in any of the CategoryRequestDTO objects is not found.
     */
    List<Category> getExistingCategories(List<CategoryRequestDTO> categories);

    /**
     * Returns the quantity of products that belong to the specified category.
     *
     * @param categoryUuId The UUID of the category to check the product quantity for.
     * @return The number of products that belong to the specified category.
     * @throws IllegalArgumentException If the provided UUID is invalid or if the category does not exist.
     */
    int getProductQuantityInCategory(UUID categoryUuId);

}
