package com.liftoff.project.service.impl;

import com.liftoff.project.controller.category.request.CategoryRequestDTO;
import com.liftoff.project.controller.category.request.CategoryUidRequestDTO;
import com.liftoff.project.controller.category.response.CategoryResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.CategoryMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_NOT_FOUND_ERROR = "Category with UUID not found: ";
    private static final String PARENT_CATEGORY_NOT_FOUND_ERROR = "Parent category with UUID not found: ";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> allCategories = categoryRepository.findByParentCategoryNull();
        List<CategoryResponseDTO> categoryResponses = new ArrayList<>();

        for (Category category : allCategories) {
            CategoryResponseDTO categoryResponse = categoryMapper.mapEntityToResponse(category);
            if (categoryResponse != null) {
                categoryResponses.add(categoryResponse);
            }
        }

        return categoryResponses;
    }

    public CategoryResponseDTO getCategoryByUuId(UUID categoryUuid) {
        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() -> new BusinessException(CATEGORY_NOT_FOUND_ERROR + categoryUuid));

        return categoryMapper.mapEntityToResponse(category);
    }

    @Override
    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO) {
        Category newCategory = categoryMapper.mapRequestToEntity(categoryRequestDTO);

        UUID newCategoryUuid = UUID.randomUUID();
        newCategory.setUId(newCategoryUuid);

        UUID parentCategoryId = categoryRequestDTO.getParentCategoryId();
        if (parentCategoryId != null) {
            Category parentCategory = categoryRepository.findByUId(parentCategoryId)
                    .orElseThrow(() -> new BusinessException(PARENT_CATEGORY_NOT_FOUND_ERROR + parentCategoryId));
            newCategory.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(newCategory);

        return categoryMapper.mapEntityToResponse(savedCategory);
    }

    @Override
    public void deleteCategoryByUuid(UUID categoryUuid) {
        Optional<Category> categoryOptional = categoryRepository.findByUId(categoryUuid);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            if (category.containsProducts() || category.containsSubcategories() || category.isSubcategory()) {
                throw new BusinessException("Category cannot be deleted due to existing products, subcategories, or being a subcategory.", HttpStatus.BAD_REQUEST);
            }

            categoryRepository.delete(category);
        } else {
            throw new BusinessException(CATEGORY_NOT_FOUND_ERROR + categoryUuid);
        }
    }

    @Override
    public CategoryResponseDTO updateCategory(UUID categoryUuid, CategoryRequestDTO categoryRequestDTO) {

        Category existingCategory = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() -> new BusinessException(CATEGORY_NOT_FOUND_ERROR + categoryUuid));

        if (isCategoryRequestWithoutUpdates(categoryRequestDTO)) {
            throw new BusinessException("Update request must include at least one field to update",
                    HttpStatus.BAD_REQUEST);
        }

        if (categoryRequestDTO.getName() != null && categoryRequestDTO.getName().isEmpty()) {
            throw new BusinessException("'name' cannot be blank",
                    HttpStatus.BAD_REQUEST);
        }

        existingCategory.setName(categoryRequestDTO
                .getName() != null ?
                categoryRequestDTO.getName() :
                existingCategory.getName());
        existingCategory.setDescription(categoryRequestDTO
                .getDescription() != null ?
                categoryRequestDTO.getDescription() :
                existingCategory.getDescription());
        existingCategory.setIcon(categoryRequestDTO
                .getIcon() != null ?
                categoryRequestDTO.getIcon() :
                existingCategory.getIcon());
        existingCategory.setImagePath(categoryRequestDTO
                .getImagePath() != null ?
                categoryRequestDTO.getImagePath() :
                existingCategory.getImagePath());

        UUID parentCategoryId = categoryRequestDTO.getParentCategoryId();
        if (parentCategoryId != null) {
            if (parentCategoryId.equals(categoryUuid)) {
                throw new BusinessException("Category cannot be its own parent.", HttpStatus.BAD_REQUEST);
            }

            Category parentCategory = categoryRepository.findByUId(parentCategoryId)
                    .orElseThrow(() ->
                            new BusinessException(PARENT_CATEGORY_NOT_FOUND_ERROR + parentCategoryId));

            existingCategory.setParentCategory(parentCategory);
        } else {
            existingCategory.setParentCategory(null);
        }

        Category savedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.mapEntityToResponse(savedCategory);
    }

    @Override
    public List<Category> getExistingCategories(List<CategoryUidRequestDTO> categories) {
        return categories.stream()
                .map(categoryRequestDTO -> {
                    UUID categoryUuid = categoryRequestDTO.getCategoryUid();
                    return categoryRepository.findByUId(categoryUuid)
                            .orElseThrow(() ->
                                    new BusinessException(CATEGORY_NOT_FOUND_ERROR + categoryUuid));
                }).toList();
    }

    @Override
    public int getProductQuantityInCategory(UUID categoryUuId) {
        Category category = categoryRepository.findByUId(categoryUuId)
                .orElseThrow(() ->
                        new BusinessException(CATEGORY_NOT_FOUND_ERROR + categoryUuId));

        return calculateProductQuantityInCategory(category);
    }

    private int calculateProductQuantityInCategory(Category category) {
        int productQuantity = category.getProducts().size();

        for (Category subcategory : category.getSubcategories()) {
            productQuantity += calculateProductQuantityInCategory(subcategory);
        }

        return productQuantity;
    }

    private boolean isCategoryRequestWithoutUpdates(CategoryRequestDTO categoryRequestDTO) {
        return categoryRequestDTO == null || (
                categoryRequestDTO.getName() == null &&
                        categoryRequestDTO.getDescription() == null &&
                        categoryRequestDTO.getIcon() == null &&
                        categoryRequestDTO.getImagePath() == null &&
                        categoryRequestDTO.getParentCategoryId() == null);
    }

}