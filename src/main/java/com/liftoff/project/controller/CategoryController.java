package com.liftoff.project.controller;

import com.liftoff.project.controller.request.CategoryRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.exception.CannotDeleteCategoryException;
import com.liftoff.project.exception.CategoryNotFoundException;
import com.liftoff.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories",
            description = "Retrieves a list of all available categories.")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryUuId}")
    @Operation(summary = "Get category by UUID",
            description = "Retrieves a category with the specified UUID.")
    public ResponseEntity<CategoryResponseDTO> getCategoryByUuid(@PathVariable UUID categoryUuId) {
        CategoryResponseDTO category = categoryService.getCategoryByUuId(categoryUuId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{categoryUuId}/product-quantity")
    @Operation(summary = "Get product quantity in category",
            description = "Retrieves the total quantity of products in a category.")
    public ResponseEntity<Integer> getProductQuantityInCategory(@PathVariable UUID categoryUuId) {
        int productQuantity = categoryService.getProductQuantityInCategory(categoryUuId);
        return ResponseEntity.ok(productQuantity);
    }

    @PostMapping
    @Operation(summary = "Add a new category",
            description = "Creates a new category with the provided details.")
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO responseDTO = categoryService.addCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/{categoryUuid}")
    @Operation(summary = "Delete a category by UUID",
            description = "Deletes a category based on the provided UUID.")
    public ResponseEntity<String> deleteCategoryByUuid(@PathVariable UUID categoryUuid) {
        try {
            categoryService.deleteCategoryByUuid(categoryUuid);
            return ResponseEntity.ok("Category with UUID " + categoryUuid + " has been successfully deleted.");
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CannotDeleteCategoryException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{categoryUuid}")
    @Operation(summary = "Update a category by UUID",
            description = "Updates a category based on the provided UUID.")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable UUID categoryUuid,
                                                              @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO responseDTO = categoryService.updateCategory(categoryUuid, categoryRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}
