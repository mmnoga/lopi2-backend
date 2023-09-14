package com.liftoff.project.controller;

import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.service.ProductPromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/promotions")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Product special offers")
public class ProductPromotionController {

    private final ProductPromotionService productPromotionService;

    @GetMapping
    @Operation(summary = "Get all products on sale")
    public ResponseEntity<List<ProductResponseDTO>> getProductsOnSale() {
        return ResponseEntity.ok(
                productPromotionService
                        .getProductsOnSale());
    }

    @GetMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Get all products on sale by category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsOnSaleByCategory(
            @PathVariable UUID categoryUuid) {
        return ResponseEntity.ok(
                productPromotionService
                        .getProductsOnSaleByCategory(categoryUuid));
    }

    @GetMapping("/{n}/by-category/{categoryUuid}")
    @Operation(summary = "Get 'n' products on sale by category")
    public ResponseEntity<List<ProductResponseDTO>> getNProductOnSaleByCategory(
            @PathVariable @Min(1) int n,
            @PathVariable UUID categoryUuid) {
        return ResponseEntity.ok(
                productPromotionService
                        .getNProductsOnSaleByCategory(n, categoryUuid));
    }

    @GetMapping("/{percentageDiscount}/by-category/{categoryUuid}/discounted")
    @Operation(summary = "Get all products on sale by category with minimum discount 'n' percents")
    public ResponseEntity<List<ProductResponseDTO>> getProductsOnSaleByCategoryWithDiscountNPercents(
            @PathVariable int percentageDiscount,
            @PathVariable UUID categoryUuid) {
        return ResponseEntity.ok(
                productPromotionService
                        .getProductsOnSaleByCategoryWithDiscountNPercents(percentageDiscount, categoryUuid));
    }

    @PutMapping("/{productUuid}/percentage-discount")
    @Operation(summary = "Set product percentage discount by product UUID")
    public ResponseEntity<ProductResponseDTO> setPercentageProductDiscount(
            @PathVariable UUID productUuid,
            @RequestParam
            @Min(value = 1, message = "Percentage discount must be at least 1")
            @Max(value = 99, message = "Percentage discount must be at most 99")
            int percentageDiscount,
            @Parameter(
                    example = "2023-12-31T23:59:59",
                    schema = @Schema(type = "string", format = "date-time"))
            @RequestParam LocalDateTime discountPriceEndDate) {
        return ResponseEntity.ok(
                productPromotionService
                        .setPercentageProductDiscountByProductUuid(
                                productUuid,
                                percentageDiscount,
                                discountPriceEndDate));
    }

    @PutMapping("/{productUuid}/amount-discount")
    @Operation(summary = "Set product discount by product UUID")
    public ResponseEntity<ProductResponseDTO> setProductDiscountPrice(
            @PathVariable UUID productUuid,
            @RequestParam
            @DecimalMin(value = "0.01", message = "Discount price must be at least 0.01")
            double discountPrice,
            @Parameter(
                    example = "2023-12-31T23:59:59",
                    schema = @Schema(type = "string", format = "date-time"))
            @RequestParam LocalDateTime discountPriceEndDate) {
        return ResponseEntity.ok(
                productPromotionService
                        .setProductDiscountPriceByProductUuid(
                                productUuid,
                                discountPrice,
                                discountPriceEndDate));
    }

    @PutMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Set product percentage discount by category UUID")
    public ResponseEntity<List<ProductResponseDTO>> setPercentageProductDiscountByCategory(
            @PathVariable UUID categoryUuid,
            @RequestParam
            @Min(value = 1, message = "Percentage discount must be at least 1")
            @Max(value = 99, message = "Percentage discount must be at most 99")
            int percentageDiscount,
            @Parameter(
                    example = "2023-12-31T23:59:59",
                    schema = @Schema(type = "string", format = "date-time"))
            @RequestParam LocalDateTime discountPriceEndDate) {
        return ResponseEntity.ok(
                productPromotionService
                        .setPercentageProductDiscountByCategory(
                                categoryUuid,
                                percentageDiscount,
                                discountPriceEndDate));
    }

    @DeleteMapping
    @Operation(summary = "Clear discount price for all products")
    public ResponseEntity<List<ProductResponseDTO>> clearProductsDiscountPrice() {
        return ResponseEntity.ok(
                productPromotionService
                        .clearProductsDiscountPrice());
    }

    @DeleteMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Clear discount price for all products by category")
    public ResponseEntity<List<ProductResponseDTO>> clearProductsDiscountPriceByCategory(
            @PathVariable UUID categoryUuid
    ) {
        return ResponseEntity.ok(
                productPromotionService
                        .clearProductsDiscountPriceByCategory(categoryUuid));
    }

}