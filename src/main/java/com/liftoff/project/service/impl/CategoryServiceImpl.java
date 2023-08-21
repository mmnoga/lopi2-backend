package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.CategoryRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.exception.CannotDeleteCategoryException;
import com.liftoff.project.exception.CategoryNotFoundException;
import com.liftoff.project.exception.InvalidParentCategoryException;
import com.liftoff.project.exception.ParentCategoryNotFoundException;
import com.liftoff.project.mapper.CategoryMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

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
                .orElseThrow(() -> new CategoryNotFoundException("Category with UUID " + categoryUuid + " not found."));
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
                    .orElseThrow(() -> new ParentCategoryNotFoundException(
                            "Parent category with UUID " + parentCategoryId + " not found."));
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
                throw new CannotDeleteCategoryException("Category cannot be deleted due to existing products, subcategories, or being a subcategory.");
            }

            categoryRepository.delete(category);
        } else {
            throw new CategoryNotFoundException("Category with UUID " + categoryUuid + " not found.");
        }
    }

    @Override
    public CategoryResponseDTO updateCategory(UUID categoryUuid, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() -> new CategoryNotFoundException("Category with UUID " + categoryUuid + " not found."));

        existingCategory.setName(categoryRequestDTO.getName());
        existingCategory.setDescription(categoryRequestDTO.getDescription());
        existingCategory.setIcon(categoryRequestDTO.getIcon());
        existingCategory.setImagePath(categoryRequestDTO.getImagePath());

        UUID parentCategoryId = categoryRequestDTO.getParentCategoryId();
        if (parentCategoryId != null) {
            if (parentCategoryId.equals(categoryUuid)) {
                throw new InvalidParentCategoryException("Category cannot be its own parent.");
            }

            Category parentCategory = categoryRepository.findByUId(parentCategoryId)
                    .orElseThrow(() ->
                            new ParentCategoryNotFoundException(
                                    "Parent category with UUID " + parentCategoryId + " not found."));

            existingCategory.setParentCategory(parentCategory);
        } else {
            existingCategory.setParentCategory(null);
        }

        Category savedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.mapEntityToResponse(savedCategory);
    }

    public List<Category> getExistingCategories(List<CategoryRequestDTO> categories) {
        List<Category> existingCategories = new ArrayList<>();
        for (CategoryRequestDTO categoryRequestDTO : categories) {
            UUID categoryUuid = categoryRequestDTO.getParentCategoryId();
            Optional<Category> categoryOptional = categoryRepository.findByUId(categoryUuid);
            if (categoryOptional.isPresent()) {
                existingCategories.add(categoryOptional.get());
            } else {
                throw new ParentCategoryNotFoundException("Parent category with UUID " + categoryUuid + " not found.");
            }
        }
        return existingCategories;
    }

    @Override
    public int getProductQuantityInCategory(UUID categoryUuId) {
        Category category = categoryRepository.findByUId(categoryUuId)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with UUID " + categoryUuId + " not found."));
        return calculateProductQuantityInCategory(category);
    }

    private int calculateProductQuantityInCategory(Category category) {
        int productQuantity = category.getProducts().size();

        for (Category subcategory : category.getSubcategories()) {
            productQuantity += calculateProductQuantityInCategory(subcategory);
        }

        return productQuantity;
    }

}
