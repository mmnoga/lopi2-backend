package com.liftoff.project.controller.product;

import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.service.ProductPromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products/promotions")
@RequiredArgsConstructor
@Tag(name = "Product Special Offers")
public class ProductPromotionController {

    private final ProductPromotionService productPromotionService;

    @GetMapping
    @Operation(summary = "Get a list of products on sale")
    public ResponseEntity<List<ProductResponseDTO>> getProductsOnSale() {
        return ResponseEntity.ok(
                productPromotionService
                        .getProductsOnSale());
    }

    @GetMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Get a list of products on sale by category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsOnSaleByCategory(
            @PathVariable UUID categoryUuid) {
        return ResponseEntity.ok(
                productPromotionService
                        .getProductsOnSaleByCategory(categoryUuid));
    }

    @GetMapping("/{n}/by-category/{categoryUuid}")
    @Operation(summary = "Get a list of 'n' products on sale by category")
    public ResponseEntity<List<ProductResponseDTO>> getNProductOnSaleByCategory(
            @PathVariable @Min(1) int n,
            @PathVariable UUID categoryUuid) {
        return ResponseEntity.ok(
                productPromotionService
                        .getNProductsOnSaleByCategory(n, categoryUuid));
    }

    @GetMapping("/{percentageDiscount}/by-category/{categoryUuid}/discounted")
    @Operation(summary = "Get a list of products on sale by category with minimum discount 'n' percents")
    public ResponseEntity<List<ProductResponseDTO>> getProductsOnSaleByCategoryWithDiscountNPercents(
            @PathVariable int percentageDiscount,
            @PathVariable UUID categoryUuid) {
        return ResponseEntity.ok(
                productPromotionService
                        .getProductsOnSaleByCategoryWithDiscountNPercents(percentageDiscount, categoryUuid));
    }

}