package com.liftoff.project.controller.product;

import com.liftoff.project.controller.product.request.PaginationParameterRequestDTO;
import com.liftoff.project.controller.product.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get a list of products",
            description = "Retrieves a paginated list of all products.")
    public ResponseEntity<PaginatedProductResponseDTO> getAllProducts(
            @Parameter(description = "Pageable object",
                    example = "{\"page\": 0, \"size\": 10, \"sort\": \"regularPrice,desc\"}")
            @PageableDefault(sort = "regularPrice")
            Pageable pageable) {

        PaginatedProductResponseDTO paginatedProducts = productService.getProducts(pageable);

        if (paginatedProducts.products().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(paginatedProducts);
        }
    }

    @GetMapping("/{productUuid}")
    @Operation(summary = "Get a product by its uuid",
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
    @Operation(summary = "Get a list of 'n' most recent added active products",
            description = "Retrieves a list of N most recent added active products.")
    public ResponseEntity<List<ProductResponseDTO>> getNRecentAddedActiveProducts(
            @RequestParam(value = "n", defaultValue = "5") @Valid @Min(1) int n) {
        List<ProductResponseDTO> products = productService.getNRecentAddedActiveProducts(n);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Get a list of products by category uuid",
            description = "Retrieves a list of products belonging to the specified category.")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable UUID categoryUuid) {

        return ResponseEntity.ok(
                productService
                        .getProductsByCategoryUuid(categoryUuid));
    }

    @GetMapping("/by-category/{categoryUuid}/sorted")
    @Operation(summary = "Get a sorted list of products at category",
            description = "Retrieves a paginated list of sorted products at category.")
    public ResponseEntity<Page<ProductResponseDTO>> getSortedProductsByCategory(
            @PathVariable UUID categoryUuid,
            @Parameter(description = "pageIndex: Page index, pageSize: Page size, " +
                    "orderColumn: Column to sort by, ascending: Flag indicating whether sorting is ascending, " +
                    "status: Status of the products to retrieve, available: Filter for product availability")
            @Valid @RequestBody PaginationParameterRequestDTO paginationParameters) {

        Page<ProductResponseDTO> paginatedProducts =
                productService
                        .getProductsByCategoryAndSort(
                                categoryUuid,
                                paginationParameters);

        if (paginatedProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(paginatedProducts);
        }
    }

}