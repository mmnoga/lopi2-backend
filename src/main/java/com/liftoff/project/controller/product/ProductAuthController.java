package com.liftoff.project.controller.product;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.controller.product.request.ProductRequestDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.exception.TechnicalException;
import com.liftoff.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import java.util.UUID;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Products <Admin>")
public class ProductAuthController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Add a new product",
            description = "Adds a new product with the provided details.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<ProductResponseDTO> addProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO responseDTO = productService
                .addProduct(productRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/activate")
    @Operation(summary = "Activate a product by its uuid",
            description = "Activates a product with the provided UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
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
    @Operation(summary = "Add an image to a product by its uuid",
            description = "Adds an image to the product with the provided UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<ProductResponseDTO> addImageToProduct(
            @PathVariable UUID productUuid,
            @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            ProductResponseDTO updatedProduct = productService.addImageToProduct(productUuid, imageFile);
            return ResponseEntity.ok(updatedProduct);
        } catch (BusinessException ex) {
            throw new ResponseStatusException(ex.getStatus(), ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new TechnicalException(ex.getMessage());
        }
    }

    @DeleteMapping("/{productUuid}/images")
    @Operation(summary = "Delete an image from a product by url",
            description = "Deletes the specified image URL from the product with the provided UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<ProductResponseDTO> deleteImageByUrlFromProduct(
            @PathVariable UUID productUuid,
            @RequestParam("imageUrl") String imageUrl) {
        try {
            ProductResponseDTO updatedProduct = productService.deleteImageByUrlFromProduct(productUuid, imageUrl);
            return ResponseEntity.ok(updatedProduct);
        } catch (BusinessException ex) {
            throw new ResponseStatusException(ex.getStatus(), ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{productUuid}")
    @Operation(summary = "Delete a product by its uuid",
            description = "Deletes the product with the provided UUID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<Void> deleteProductByUuid(@PathVariable UUID productUuid) {
        try {
            productService.deleteProductByUuId(productUuid);
            return ResponseEntity.noContent().build();
        } catch (BusinessException ex) {
            throw new ResponseStatusException(ex.getStatus(), ex.getMessage(), ex);
        }
    }

    @PutMapping("/{productUuid}")
    @Operation(summary = "Update a product by its uuid",
            description = "Updates the product with the provided UUID using the given information.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID productUuid,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        try {
            ProductResponseDTO updatedProduct = productService
                    .updateProductByUuid(productUuid, productRequestDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (BusinessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}