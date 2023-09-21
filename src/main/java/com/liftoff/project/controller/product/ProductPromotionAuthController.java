package com.liftoff.project.controller.product;

import com.liftoff.project.configuration.security.annotations.HasAdminRole;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.service.ProductPromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products/promotions")
@RequiredArgsConstructor
@Tag(name = "Product Special Offers <Admin>")
public class ProductPromotionAuthController {

    private final ProductPromotionService productPromotionService;

    @PutMapping("/{productUuid}/percentage-discount")
    @Operation(summary = "Set product percentage discount by product uuid",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
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
    @Operation(summary = "Set a product discount by product uuid",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
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
    @Operation(summary = "Set a product percentage discount by category uuid",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
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
    @Operation(summary = "Clear a discount price for all products",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<List<ProductResponseDTO>> clearProductsDiscountPrice() {
        return ResponseEntity.ok(
                productPromotionService
                        .clearProductsDiscountPrice());
    }

    @DeleteMapping("/by-category/{categoryUuid}")
    @Operation(summary = "Clear a discount price for all products by category",
            security = @SecurityRequirement(name = "bearerAuth"))
    @HasAdminRole
    public ResponseEntity<List<ProductResponseDTO>> clearProductsDiscountPriceByCategory(
            @PathVariable UUID categoryUuid
    ) {
        return ResponseEntity.ok(
                productPromotionService
                        .clearProductsDiscountPriceByCategory(categoryUuid));
    }

}