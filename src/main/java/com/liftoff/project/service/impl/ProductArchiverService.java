package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductArchiverService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO archiveProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO,
            Set<Category> existingCategories) {
        switch (existingProduct.getStatus()) {
            case IN_PREPARATION:
                return archiveInPreparationProduct(existingProduct, productRequestDTO, existingCategories);
            case ACTIVE:
                return archiveActiveProduct(existingProduct, productRequestDTO, existingCategories);
            case CLOSED:
                return archiveClosedProduct(productRequestDTO, existingCategories);
            default:
                throw new IllegalArgumentException("Invalid product status");
        }
    }

    private ProductResponseDTO archiveInPreparationProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO,
            Set<Category> existingCategories) {
        updateProductFromRequest(existingProduct, productRequestDTO);

        Product updatedProduct = productRepository.save(existingProduct);
        updatedProduct.setCategories(existingCategories);
        updatedProduct.setUpdatedAt(Instant.now());

        return productMapper.mapEntityToResponse(updatedProduct);
    }

    private ProductResponseDTO archiveActiveProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO,
            Set<Category> existingCategories) {
        existingProduct.setStatus(ProductStatus.CLOSED);
        existingProduct.setArchivedAt(Instant.now());

        Product newProductFromActiveProduct = Product.builder()
                .uId(UUID.randomUUID())
                .name(productRequestDTO.getName())
                .sku(productRequestDTO.getSku())
                .description(productRequestDTO.getDescription())
                .regularPrice(productRequestDTO.getRegularPrice())
                .discountPrice(productRequestDTO.getDiscountPrice())
                .lowestPrice(productRequestDTO.getLowestPrice())
                .shortDescription(productRequestDTO.getShortDescription())
                .note(productRequestDTO.getNote())
                .status(ProductStatus.ACTIVE)
                .categories(existingCategories)
                .updatedAt(Instant.now())
                .build();

        Product savedNewProduct = productRepository.save(newProductFromActiveProduct);

        return productMapper.mapEntityToResponse(savedNewProduct);
    }

    private ProductResponseDTO archiveClosedProduct(
            ProductRequestDTO productRequestDTO,
            Set<Category> existingCategories) {
        Product newProductFromActiveProduct = Product.builder()
                .uId(UUID.randomUUID())
                .name(productRequestDTO.getName())
                .sku(productRequestDTO.getSku())
                .description(productRequestDTO.getDescription())
                .regularPrice(productRequestDTO.getRegularPrice())
                .discountPrice(productRequestDTO.getDiscountPrice())
                .lowestPrice(productRequestDTO.getLowestPrice())
                .shortDescription(productRequestDTO.getShortDescription())
                .note(productRequestDTO.getNote())
                .status(ProductStatus.ACTIVE)
                .categories(existingCategories)
                .updatedAt(Instant.now())
                .build();

        Product savedNewProduct = productRepository.save(newProductFromActiveProduct);

        return productMapper.mapEntityToResponse(savedNewProduct);
    }

    private void updateProductFromRequest(Product product, ProductRequestDTO productRequestDTO) {
        product.setName(productRequestDTO.getName());
        product.setSku(productRequestDTO.getSku());
        product.setDescription(productRequestDTO.getDescription());
        product.setRegularPrice(productRequestDTO.getRegularPrice());
        product.setDiscountPrice(productRequestDTO.getDiscountPrice());
        product.setDiscountPriceEndDate(productRequestDTO.getDiscountPriceEndDate());
        product.setLowestPrice(productRequestDTO.getLowestPrice());
        product.setShortDescription(productRequestDTO.getShortDescription());
        product.setNote(productRequestDTO.getNote());
        product.setProductscol(productRequestDTO.getProductscol());
        product.setQuantity(productRequestDTO.getQuantity());
    }

}
