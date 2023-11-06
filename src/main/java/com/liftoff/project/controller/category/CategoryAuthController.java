package com.liftoff.project.controller.category;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.controller.category.request.CategoryRequestDTO;
import com.liftoff.project.controller.category.response.CategoryResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@Tag(name = "Categories <Admin>")
public class CategoryAuthController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Add a new category",
            description = "Creates a new category with the provided details.",
            security = @SecurityRequirement(name = "bearerAuth"))
//    @HasAdminRole
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO responseDTO = categoryService.addCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/{categoryUuid}")
    @Operation(summary = "Delete a category by its uuid",
            description = "Deletes a category based on the provided UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<String> deleteCategoryByUuid(@PathVariable UUID categoryUuid) {
        try {
            categoryService.deleteCategoryByUuid(categoryUuid);
            return ResponseEntity.ok("Category with UUID " + categoryUuid + " has been successfully deleted.");
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{categoryUuid}")
    @Operation(summary = "Update a category by its uuid",
            description = "Updates a category based on the provided UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable UUID categoryUuid,
                                                              @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO responseDTO = categoryService.updateCategory(categoryUuid, categoryRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}