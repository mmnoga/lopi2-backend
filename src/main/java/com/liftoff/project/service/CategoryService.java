package com.liftoff.project.service;

import com.liftoff.project.controller.category.request.CategoryRequestDTO;
import com.liftoff.project.controller.category.request.CategoryUidRequestDTO;
import com.liftoff.project.controller.category.response.CategoryResponseDTO;
import com.liftoff.project.exception.BusinessException;
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
     * @throws BusinessException If the category with the given UUID is not found.
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
     * @param categoryUuid       The UUID of the category to be updated.
     * @param categoryRequestDTO The CategoryRequestDTO object containing the updated details of the category.
     * @return The CategoryResponseDTO object representing the updated category.
     * @throws BusinessException If the category with the given UUID is not found or
     *                           there is a validation error.
     *                           The exception message provides details about the specific issue.
     */
    CategoryResponseDTO updateCategory(UUID categoryUuid, CategoryRequestDTO categoryRequestDTO);

    /**
     * Deletes a category with the specified UUID.
     *
     * @param categoryUuid The UUID of the category to be deleted.
     * @throws BusinessException If the category with the given UUID is not found.
     */
    void deleteCategoryByUuid(UUID categoryUuid);

    /**
     * Retrieves existing Category entities based on a list of CategoryUidRequestDTO.
     * <p>
     * This method iterates through the provided list of CategoryUidRequestDTO objects,
     * retrieves the corresponding Category entity for each Category UID, and collects
     * them into a list. If any Category UID is not found in the repository, it throws
     * a BusinessException with an appropriate error message.
     *
     * @param categories A list of CategoryUidRequestDTO objects containing Category UIDs.
     * @return A list of existing Category entities.
     * @throws BusinessException if any of the specified Category UIDs are not found.
     */
    List<Category> getExistingCategories(List<CategoryUidRequestDTO> categories);

    /**
     * Returns the quantity of products that belong to the specified category.
     *
     * @param categoryUuId The UUID of the category to check the product quantity for.
     * @return The number of products that belong to the specified category.
     * @throws BusinessException If the provided UUID is invalid or if the category does not exist.
     */
    int getProductQuantityInCategory(UUID categoryUuId);

}