package com.liftoff.project.controller;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.product.ProductNotFoundException;
import com.liftoff.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products",
            description = "Retrieves a paginated list of all products.")
    public ResponseEntity<PaginatedProductResponseDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name")
            @Parameter(description = "Sort by name or regularPrice") String sortType,
            @RequestParam(defaultValue = "asc")
            @Parameter(description = "Sort order: asc or desc") String sortOrder
    ) {
        Sort.Direction direction = sortOrder
                .equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortType));

        PaginatedProductResponseDTO paginatedProducts = productService.getProducts(pageable);

        if (paginatedProducts.products().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(paginatedProducts);
        }
    }

    @GetMapping("/{productUuid}")
    @Operation(summary = "Get product by UUID",
            description = "Retrieves product information by its UUID.")
    public ResponseEntity<ProductResponseDTO> getProductByUuid(@PathVariable UUID productUuid) {
        ProductResponseDTO product = productService.getProductByUuid(productUuid);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recent-added")
    @Operation(summary = "Get N most recent added active products",
            description = "Retrieves a list of N most recent added active products.")
    public ResponseEntity<List<ProductResponseDTO>> getNRecentAddedActiveProducts(
            @RequestParam(value = "n", defaultValue = "5") @Valid @Min(1) int n) {
        List<ProductResponseDTO> products = productService.getNRecentAddedActiveProducts(n);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Add a new product",
            description = "Adds a new product with the provided details.")
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO responseDTO = productService.addProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/activate")
    @Operation(summary = "Activate a product by UUID",
            description = "Activates a product with the provided UUID.")
    public ResponseEntity<ProductResponseDTO> activateProductByUuid(@RequestParam UUID productUuid) {
        ProductResponseDTO product = productService.getProductByUuid(productUuid);
        if (product != null) {
            ProductResponseDTO activatedProduct
                    = productService.activateProduct(product);
            return ResponseEntity.ok(activatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{productUuid}/images", consumes = {"multipart/form-data"})
    @Operation(summary = "Add an image to a product by UUID",
            description = "Adds an image to the product with the provided UUID.")
    public ResponseEntity<ProductResponseDTO> addImageToProduct(
            @PathVariable UUID productUuid,
            @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            ProductResponseDTO updatedProduct = productService.addImageToProduct(productUuid, imageFile);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException ex) {
            throw new ResponseStatusException(ex.getStatus(), ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @DeleteMapping("/{productUuid}/images")
    @Operation(summary = "Delete an image from a product by URL",
            description = "Deletes the specified image URL from the product with the provided UUID.")
    public ResponseEntity<ProductResponseDTO> deleteImageByUrlFromProduct(
            @PathVariable UUID productUuid,
            @RequestParam("imageUrl") String imageUrl) {
        try {
            ProductResponseDTO updatedProduct = productService.deleteImageByUrlFromProduct(productUuid, imageUrl);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException ex) {
            throw new ResponseStatusException(ex.getStatus(), ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{productUuid}")
    @Operation(summary = "Delete a product by UUID",
            description = "Deletes the product with the provided UUID.")
    public ResponseEntity<Void> deleteProductByUuid(@PathVariable UUID productUuid) {
        try {
            productService.deleteProductByUuId(productUuid);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException ex) {
            throw new ResponseStatusException(ex.getStatus(), ex.getMessage(), ex);
        }
    }

    @PutMapping("/{productUuid}")
    @Operation(summary = "Update a product by UUID",
            description = "Updates the product with the provided UUID using the given information.")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID productUuid,
                                                            @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO updatedProduct = productService.updateProductByUuid(productUuid, productRequestDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Get products by category ID",
            description = "Retrieves a list of products belonging to the specified category.")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable UUID categoryUuid) {

        return ResponseEntity.ok(
                productService
                        .getProductsByCategoryUuid(categoryUuid));
    }

}