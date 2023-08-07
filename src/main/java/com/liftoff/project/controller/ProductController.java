package com.liftoff.project.controller;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.CategoryNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PaginatedProductResponseDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginatedProductResponseDTO paginatedProducts = productService.getProducts(page, size);

        if (paginatedProducts.products().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(paginatedProducts);
        }
    }

    @GetMapping("/{productUuid}")
    public ResponseEntity<ProductResponseDTO> getProductByUuid(@PathVariable UUID productUuid) {
        ProductResponseDTO product = productService.getProductByUuid(productUuid);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recent-added")
    public ResponseEntity<List<ProductResponseDTO>> getNRecentAddedActiveProducts(
            @RequestParam(value = "n", defaultValue = "5") @Valid @Min(1) int n) {
        List<ProductResponseDTO> products = productService.getNRecentAddedActiveProducts(n);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO responseDTO = productService.addProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/{productUuid}")
    public ResponseEntity<Void> deleteProductByUuid(@PathVariable UUID productUuid) {
        try {
            productService.deleteProductByUuId(productUuid);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productUuid}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID productUuid,
                                                            @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO updatedProduct = productService.updateProductByUuid(productUuid, productRequestDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        try {
            List<ProductResponseDTO> products = productService.getProductsByCategoryId(categoryId);
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(products);
            }
        } catch (CategoryNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}