package com.liftoff.project.controller.category;

import com.liftoff.project.controller.category.response.CategoryResponseDTO;
import com.liftoff.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get a list of categories",
            description = "Retrieves a list of all available categories.")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryUuId}")
    @Operation(summary = "Get a category by its uuid",
            description = "Retrieves a category with the specified UUID.")
    public ResponseEntity<CategoryResponseDTO> getCategoryByUuid(@PathVariable UUID categoryUuId) {
        CategoryResponseDTO category = categoryService.getCategoryByUuId(categoryUuId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    @GetMapping("/{categoryUuId}/product-quantity")
    @Operation(summary = "Get a product quantity in category",
            description = "Retrieves the total quantity of products in a category.")
    public ResponseEntity<Integer> getProductQuantityInCategory(@PathVariable UUID categoryUuId) {
        int productQuantity = categoryService.getProductQuantityInCategory(categoryUuId);

        return ResponseEntity.ok(productQuantity);
    }

}
