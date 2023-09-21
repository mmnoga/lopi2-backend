package com.liftoff.project.service.impl;

import com.liftoff.project.controller.product.request.ProductRequestDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.ImageAsset;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductArchiverService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO archiveProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO,
            List<Category> existingCategories) {
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
            List<Category> existingCategories) {
        updateProductFromRequest(existingProduct, productRequestDTO);

        Product updatedProduct = productRepository.save(existingProduct);
        updatedProduct.setCategories(existingCategories);
        updatedProduct.setUpdatedAt(Instant.now());

        return productMapper.mapEntityToResponse(updatedProduct);
    }

    private ProductResponseDTO archiveActiveProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO,
            List<Category> existingCategories) {
        existingProduct.setStatus(ProductStatus.CLOSED);
        existingProduct.setArchivedAt(Instant.now());

        List<ImageAsset> newImages = existingProduct.getImages()
                .stream()
                .map(image -> ImageAsset.builder().assetUrl(image.getAssetUrl()).build())
                .collect(Collectors.toList());

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
                .images(newImages)
                .updatedAt(Instant.now())
                .build();

        Product savedNewProduct = productRepository.save(newProductFromActiveProduct);

        return productMapper.mapEntityToResponse(savedNewProduct);
    }

    private ProductResponseDTO archiveClosedProduct(
            ProductRequestDTO productRequestDTO,
            List<Category> existingCategories) {
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

        if (productRequestDTO.getImages() != null) {
            List<ImageAsset> updatedImages = productRequestDTO.getImages()
                    .stream()
                    .map(imageDto -> ImageAsset.builder()
                            .assetUrl(imageDto.getImageUrl())
                            .build())
                    .collect(Collectors.toList());
            product.setImages(updatedImages);
        }
    }

}
