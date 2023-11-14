package com.liftoff.project.service.impl;

import com.liftoff.project.controller.product.request.ProductRequestDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.mapper.CategoryMapper;
import com.liftoff.project.mapper.ImageMapper;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.ImageAsset;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.repository.ImageAssetRepository;
import com.liftoff.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductArchiverService {

    private final ProductRepository productRepository;
    private final ImageAssetRepository imageAssetRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ImageMapper imageMapper;

    public ProductResponseDTO archiveProduct(Product existingProduct, ProductRequestDTO productRequestDTO) {
        return switch (existingProduct.getStatus()) {
            case IN_PREPARATION -> archiveInPreparationProduct(existingProduct, productRequestDTO);
            case ACTIVE -> archiveActiveProduct(existingProduct, productRequestDTO);
            case CLOSED -> archiveClosedProduct(existingProduct, productRequestDTO);
        };
    }

    private ProductResponseDTO archiveInPreparationProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO) {

        updateProductFromRequest(existingProduct, productRequestDTO);

        Product updatedProduct = productRepository.save(existingProduct);
        updatedProduct.setUpdatedAt(Instant.now());

        return productMapper.mapEntityToResponse(updatedProduct);
    }

    private ProductResponseDTO archiveActiveProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO) {

        existingProduct.setStatus(ProductStatus.CLOSED);
        existingProduct.setArchivedAt(Instant.now());

        Product newProduct = productFromRequestDTO(existingProduct, productRequestDTO);
        newProduct.setUId(UUID.randomUUID());
        newProduct.setStatus(ProductStatus.ACTIVE);

        Product savedNewProduct = productRepository.save(newProduct);
        savedNewProduct.setUpdatedAt(Instant.now());

        return productMapper.mapEntityToResponse(savedNewProduct);
    }

    private ProductResponseDTO archiveClosedProduct(
            Product existingProduct,
            ProductRequestDTO productRequestDTO) {

        Product newProduct = productFromRequestDTO(existingProduct, productRequestDTO);
        newProduct.setUId(UUID.randomUUID());
        newProduct.setStatus(ProductStatus.IN_PREPARATION);

        Product savedNewProduct = productRepository.save(newProduct);
        savedNewProduct.setUpdatedAt(Instant.now());

        return productMapper.mapEntityToResponse(savedNewProduct);
    }

    private void updateProductFromRequest(
            Product product,
            ProductRequestDTO productRequestDTO) {

        if (productRequestDTO.getName() != null) {
            product.setName(productRequestDTO.getName());
        }
        if (productRequestDTO.getSku() != null) {
            product.setSku(productRequestDTO.getSku());
        }
        if (productRequestDTO.getRegularPrice() != null) {
            product.setRegularPrice(productRequestDTO.getRegularPrice());
        }
        if (productRequestDTO.getDiscountPrice() != null) {
            product.setDiscountPrice(productRequestDTO.getDiscountPrice());
        }
        if (productRequestDTO.getDiscountPriceEndDate() != null) {
            product.setDiscountPriceEndDate(productRequestDTO.getDiscountPriceEndDate());
        }
        if (productRequestDTO.getDescription() != null) {
            product.setDescription(productRequestDTO.getDescription());
        }
        if (productRequestDTO.getShortDescription() != null) {
            product.setShortDescription(productRequestDTO.getShortDescription());
        }
        if (productRequestDTO.getNote() != null) {
            product.setNote(productRequestDTO.getNote());
        }
        if (productRequestDTO.getProductscol() != null) {
            product.setProductscol(productRequestDTO.getProductscol());
        }
        if (productRequestDTO.getQuantity() != null) {
            product.setQuantity(productRequestDTO.getQuantity());
        }
        if (productRequestDTO.getImages() != null) {
            List<ImageAsset> existingImages = product.getImages()
                    .stream()
                    .map(image -> {
                        ImageAsset newImage = ImageAsset.builder()
                                .assetUrl(image.getAssetUrl())
                                .build();
                        return imageAssetRepository.save(newImage);
                    })
                    .toList();
            product.setImages(existingImages);
        }
        if (productRequestDTO.getCategories() != null) {
            List<Category> categoryList = productRequestDTO.getCategories().stream()
                    .map(categoryMapper::mapUidRequestToEntity)
                    .toList();
            product.setCategories(new ArrayList<>(categoryList));
        }
    }

    private Product productFromRequestDTO(
            Product existingProduct,
            ProductRequestDTO productRequestDTO) {

        List<ImageAsset> existingImages = existingProduct.getImages()
                .stream()
                .map(image -> {
                    ImageAsset newImage = ImageAsset.builder()
                            .assetUrl(image.getAssetUrl())
                            .build();
                    return imageAssetRepository.save(newImage);
                })
                .toList();

        Product newProduct = Product.builder()
                .updatedAt(Instant.now())
                .categories(new ArrayList<>())
                .images(existingImages)
                .build();

        if (productRequestDTO.getName() != null) {
            newProduct.setName(productRequestDTO.getName());
        } else {
            newProduct.setName(existingProduct.getName());
        }
        if (productRequestDTO.getSku() != null) {
            newProduct.setSku(productRequestDTO.getSku());
        } else {
            newProduct.setSku(existingProduct.getSku());
        }
        if (productRequestDTO.getDescription() != null) {
            newProduct.setDescription(productRequestDTO.getDescription());
        } else {
            newProduct.setDescription(existingProduct.getDescription());
        }
        if (productRequestDTO.getRegularPrice() != null) {
            newProduct.setRegularPrice(productRequestDTO.getRegularPrice());
        } else {
            newProduct.setRegularPrice(existingProduct.getRegularPrice());
        }
        if (productRequestDTO.getDiscountPrice() != null) {
            newProduct.setDiscountPrice(productRequestDTO.getDiscountPrice());
        } else {
            newProduct.setDiscountPrice(existingProduct.getDiscountPrice());
        }
        if (productRequestDTO.getDiscountPriceEndDate() != null) {
            newProduct.setDiscountPriceEndDate(productRequestDTO.getDiscountPriceEndDate());
        } else {
            newProduct.setDiscountPriceEndDate(existingProduct.getDiscountPriceEndDate());
        }
        if (productRequestDTO.getShortDescription() != null) {
            newProduct.setShortDescription(productRequestDTO.getShortDescription());
        } else {
            newProduct.setShortDescription(existingProduct.getShortDescription());
        }
        if (productRequestDTO.getNote() != null) {
            newProduct.setNote(productRequestDTO.getNote());
        } else {
            newProduct.setNote(existingProduct.getNote());
        }
        if (productRequestDTO.getProductscol() != null) {
            newProduct.setProductscol(productRequestDTO.getProductscol());
        } else {
            newProduct.setProductscol(existingProduct.getProductscol());
        }
        if (productRequestDTO.getQuantity() != null) {
            newProduct.setQuantity(productRequestDTO.getQuantity());
        } else {
            newProduct.setQuantity(existingProduct.getQuantity());
        }
        if (productRequestDTO.getCategories() != null) {
            List<Category> categoryList = productRequestDTO.getCategories().stream()
                    .map(categoryMapper::mapUidRequestToEntity)
                    .toList();
            newProduct.setCategories(new ArrayList<>(categoryList));
        } else {
            newProduct.setCategories(new ArrayList<>(
                    existingProduct.getCategories()));
        }
        if (productRequestDTO.getImages() != null) {
            List<ImageAsset> imageList = productRequestDTO.getImages().stream()
                    .map(imageMapper::mapRequestToEntity)
                    .toList();
            newProduct.setImages(imageList);
        }

        return newProduct;
    }

}