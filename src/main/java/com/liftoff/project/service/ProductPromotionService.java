package com.liftoff.project.service;

import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.category.CategoryNotFoundException;
import com.liftoff.project.exception.product.InvalidDiscountException;
import com.liftoff.project.exception.product.ProductNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductPromotionService {

    /**
     * Retrieves a list of products that are currently on sale.
     *
     * This method finds products with a discount price greater than 0.00,
     * a discount price end date after the current date and time, and a non-null discount price end date.
     *
     * @return A list of ProductResponseDTO objects representing products on sale.
     */
    List<ProductResponseDTO> getProductsOnSale();

    /**
     * Retrieves all products on sale for a specified category and its nested subcategories.
     *
     * @param categoryUuid The UUID of the category for which products on sale are to be retrieved.
     * @return A list of products on sale (including those from nested categories).
     * @throws CategoryNotFoundException If the category with the given UUID does not exist.
     */
    List<ProductResponseDTO> getProductsOnSaleByCategory(UUID categoryUuid);

    /**
     * Retrieves up to 'n' products on sale for a specified category and its nested subcategories.
     *
     * @param n The maximum number of products to retrieve on sale.
     * @param categoryUuid The UUID of the category for which products on sale are to be retrieved.
     * @return A list of up to 'n' products on sale (including those from nested categories).
     * @throws CategoryNotFoundException If the category with the given UUID does not exist.
     */
    List<ProductResponseDTO> getNProductsOnSaleByCategory(int n, UUID categoryUuid);

    /**
     * Retrieves all products on sale in a specified category and its nested subcategories with a discount equal to or greater than 'n' percent.
     *
     * @param percentageDiscount The minimum percentage discount for products to be included.
     * @param categoryUuid The UUID of the category for which products on sale are to be retrieved.
     * @return A list of products on sale with the specified discount percentage (including those from nested categories).
     * @throws CategoryNotFoundException If the category with the given UUID does not exist.
     */
    List<ProductResponseDTO> getProductsOnSaleByCategoryWithDiscountNPercents(
            int percentageDiscount, UUID categoryUuid);

    /**
     * Sets the percentage discount for a product identified by its UUID, along with the discount end date.
     * Calculates the discounted price based on the provided percentage and updates the product accordingly.
     *
     * @param productUuid         The UUID of the product to apply the discount to.
     * @param percentageDiscount  The percentage discount to apply (must be between 1 and 99).
     * @param discountPriceEndDate The end date for the discount period.
     *                            It should be in ISO date-time format (e.g., "2023-12-31T23:59:59").
     * @return A {@link ProductResponseDTO} containing the updated product information.
     * @throws ProductNotFoundException   If the product with the specified UUID is not found.
     * @throws InvalidDiscountException  If the percentage discount is not within the valid range (1-99).
     */
    ProductResponseDTO setPercentageProductDiscountByProductUuid(
            UUID productUuid, int percentageDiscount, LocalDateTime discountPriceEndDate);

    /**
     * Sets the discount price for a product identified by its UUID, along with the discount end date.
     * Validates that the discount price is lower than the regular price before updating the product.
     *
     * @param productUuid         The UUID of the product to apply the discount to.
     * @param discountPrice       The discounted price for the product (must be lower than the regular price).
     * @param discountPriceEndDate The end date for the discount period.
     *                            It should be in ISO date-time format (e.g., "2023-12-31T23:59:59").
     * @return A {@link ProductResponseDTO} containing the updated product information.
     * @throws ProductNotFoundException  If the product with the specified UUID is not found.
     * @throws InvalidDiscountException If the discount price is greater than or equal to the regular price.
     */
    ProductResponseDTO setProductDiscountPriceByProductUuid(
            UUID productUuid, double discountPrice, LocalDateTime discountPriceEndDate);

    /**
     * Sets a percentage discount on products within a specific category and its subcategories.
     * The discount will be applied to all products, and their discount end date will be updated.
     *
     * @param categoryUuid         The UUID of the category for which the discount should be applied.
     * @param percentageDiscount   The percentage discount to apply to the products.
     * @param discountPriceEndDate The end date for the discount period.
     * @return A list of {@link ProductResponseDTO} objects representing the updated products with discounts.
     * @throws CategoryNotFoundException If the specified category UUID does not correspond to any existing category.
     * @throws InvalidDiscountException If the calculated discount price is greater than
     * or equal to the regular price for any product.
     */
    List<ProductResponseDTO> setPercentageProductDiscountByCategory(
            UUID categoryUuid, int percentageDiscount, LocalDateTime discountPriceEndDate);

    /**
     * Clears the discount price and discount price end date for products
     * with a non-null discount price
     * greater than 0.0.
     *
     * @return A list of products with cleared discount price.
     */
    List<ProductResponseDTO> clearProductsDiscountPrice();

    /**
     * Clears the discount prices and discount price end dates for products
     * within the specified category and its subcategories.
     *
     * @param categoryUuid          The UUID of the category for which to clear discount prices.
     * @return A list of products with cleared discount prices.
     */
    List<ProductResponseDTO> clearProductsDiscountPriceByCategory(UUID categoryUuid);

}
