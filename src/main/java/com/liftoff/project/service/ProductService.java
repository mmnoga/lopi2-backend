package com.liftoff.project.service;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.CategoryNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    /**
     * Retrieves a paginated list of products as ProductResponseDTO object.
     *
     * @param page The page number to retrieve.
     * @param size The number of products per page.
     * @return The list of ProductResponseDTO objects representing the products on the specified page.
     */
    PaginatedProductResponseDTO getProducts(int page, int size);

    /**
     * Retrieves a specific product by its UUID as the ProductResponseDTO object.
     *
     * @param productUuid The UUID of the product to be retrieved.
     * @return The ProductResponseDTO object representing the product with specified UUID.
     * @throws ProductNotFoundException If the product with the given UUID is not found.
     */
    ProductResponseDTO getProductByUuid(UUID productUuid);

    /**
     * Retrieves a list of products that belong to the category with the specified UUID.
     *
     * @param categoryId The UUID of the category for witch the products are to be retrieved.
     * @return The list of ProductResponseDTO objects representing the products in the specified category.
     * @throws CategoryNotFoundException If the category with the given UUID is not found.
     */
    List<ProductResponseDTO> getProductsByCategoryId(UUID categoryId);

    /**
     * Retrieves a list of n most recently added active products.
     *
     * @param n The number of most recent products to fetch.
     * @return The list of ProductResponseDTO objects representing the most recently added active products.
     * The list may be empty if there are no active products.
     */

    List<ProductResponseDTO> getNRecentAddedActiveProducts(int n);

    /**
     * Adds a new product based on the provided ProductRequestDTO object
     * and returns the created product as the ProductResponseDTO object.
     *
     * @param productRequestDTO The ProductRequestDOT object containing the details of the new product.
     * @return The ProductResponseDTO object representing the newly added product.
     */
    ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO);

    /**
     * Updates an existing product with the given UUID based on the provided product request data.
     *
     * @param productUuid       The UUID of the product to be updated.
     * @param productRequestDTO The ProductRequestDTO object containing the updated details of the product.
     * @return The ProductResponseDTO object representing the updated product.
     * @throws ProductNotFoundException If the product with the given UUID is not found.
     */
    ProductResponseDTO updateProductByUuid(UUID productUuid, ProductRequestDTO productRequestDTO);

    /**
     * Deletes a product with the specified UUID.
     *
     * @param productUuId The UUID of the product to be deleted.
     * @throws ProductNotFoundException If the product with given UUID is not found.
     */
    void deleteProductByUuId(UUID productUuId);

    /**
     * Activate an existing product.
     *
     * @param product The product to be updated.
     * @return The ProductResponseDTO object representing the activated product.
     * @throws ProductNotFoundException If the product with the given UUID is not found.
     */
    ProductResponseDTO activateProduct(ProductResponseDTO product);

}