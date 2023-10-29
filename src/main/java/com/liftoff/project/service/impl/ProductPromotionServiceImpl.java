package com.liftoff.project.service.impl;

import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.service.ProductPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPromotionServiceImpl implements ProductPromotionService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponseDTO> getProductsOnSale() {

        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Product> productsOnSale = productRepository
                .findProductsByDiscountPriceGreaterThanAndDiscountPriceEndDateAfterAndDiscountPriceEndDateIsNotNull(
                        0.00, currentDateTime);

        return productsOnSale.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsOnSaleByCategory(
            UUID categoryUuid) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() ->
                        new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> productsOnSale = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();

        addProductsOnSaleFromCategory(category, productsOnSale, currentDateTime);

        return productsOnSale.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getNProductsOnSaleByCategory(
            int n,
            UUID categoryUuid) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() ->
                        new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> productsOnSale = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();

        addProductsOnSaleFromCategory(category, productsOnSale, currentDateTime);

        List<Product> limitedProductsOnSale = productsOnSale.stream()
                .filter(product ->
                        product.getDiscountPrice() > 0.00 &&
                                product.getDiscountPriceEndDate() != null &&
                                product.getDiscountPriceEndDate().isAfter(currentDateTime))
                .limit(n)
                .toList();

        return limitedProductsOnSale.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsOnSaleByCategoryWithDiscountNPercents(
            int percentageDiscount,
            UUID categoryUuid) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() ->
                        new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> productsOnSale = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();

        addProductsOnSaleFromCategoryWithDiscountPercentage(
                percentageDiscount,
                category,
                productsOnSale,
                currentDateTime);

        List<Product> limitedProductsOnSale = productsOnSale.stream()
                .filter(product ->
                        product.getDiscountPrice() > 0.00 &&
                                product.getDiscountPriceEndDate() != null &&
                                product.getDiscountPriceEndDate().isAfter(currentDateTime))
                .toList();

        return limitedProductsOnSale.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    @Override
    public ProductResponseDTO setPercentageProductDiscountByProductUuid(
            UUID productUuid,
            int percentageDiscount,
            LocalDateTime discountPriceEndDate) {

        Product product = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found"));

        Double regularPrice = product.getRegularPrice();
        Double discountPrice = regularPrice - (regularPrice * (percentageDiscount / 100.0));

        product.setDiscountPrice(discountPrice);
        product.setDiscountPriceEndDate(discountPriceEndDate);
        if (product.getLowestPrice() > discountPrice) {
            product.setLowestPrice(discountPrice);
        }

        Product savedProduct = productRepository.save(product);

        return productMapper
                .mapEntityToResponse(savedProduct);
    }

    @Override
    public ProductResponseDTO setProductDiscountPriceByProductUuid(
            UUID productUuid,
            double discountPrice,
            LocalDateTime discountPriceEndDate) {

        Product product = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found"));

        if (discountPrice >= product.getRegularPrice()) {
            throw new BusinessException("Discount price cannot be greater than or equal to regular price");
        }

        product.setDiscountPrice(discountPrice);
        product.setDiscountPriceEndDate(discountPriceEndDate);
        if (product.getLowestPrice() > discountPrice) {
            product.setLowestPrice(discountPrice);
        }

        Product savedProduct = productRepository.save(product);

        return productMapper
                .mapEntityToResponse(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> setPercentageProductDiscountByCategory(
            UUID categoryUuid,
            int percentageDiscount,
            LocalDateTime discountPriceEndDate) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() -> new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> productsToUpdate = new ArrayList<>();

        applyPercentageDiscountToCategory(
                category,
                percentageDiscount,
                discountPriceEndDate,
                productsToUpdate);

        List<Product> savedProducts = productRepository.saveAll(productsToUpdate);

        return savedProducts.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> clearProductsDiscountPrice() {

        List<Product> productsToClear = productRepository
                .findProductsByDiscountPriceNotNullAndDiscountPriceGreaterThan(0.0);

        for (Product product : productsToClear) {
            product.setDiscountPrice(0.0);
            product.setDiscountPriceEndDate(null);
        }

        List<Product> savedProducts = productRepository.saveAll(productsToClear);

        return savedProducts.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> clearProductsDiscountPriceByCategory(
            UUID categoryUuid) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() -> new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> productsToClear = new ArrayList<>();
        collectProductsToClearInCategory(category, productsToClear);

        for (Product product : productsToClear) {
            product.setDiscountPrice(0.0);
            product.setDiscountPriceEndDate(null);
        }

        List<Product> savedProducts = productRepository.saveAll(productsToClear);

        return savedProducts.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
    }

    private void addProductsOnSaleFromCategory(
            Category category,
            List<Product> productsOnSale,
            LocalDateTime currentDateTime) {

        List<Product> productsInCategory = category.getProducts();

        if (productsInCategory != null) {
            productsOnSale.addAll(productsInCategory.stream()
                    .filter(product ->
                            product.getDiscountPrice() > 0.00 &&
                                    product.getDiscountPriceEndDate() != null &&
                                    product.getDiscountPriceEndDate().isAfter(currentDateTime))
                    .toList());
        }

        List<Category> subcategories = category.getSubcategories();
        if (subcategories != null) {
            for (Category subcategory : subcategories) {
                addProductsOnSaleFromCategory(subcategory, productsOnSale, currentDateTime);
            }
        }
    }

    private void addProductsOnSaleFromCategoryWithDiscountPercentage(
            int percentageDiscount,
            Category category,
            List<Product> productsOnSale,
            LocalDateTime currentDateTime) {

        List<Product> productsInCategory = category.getProducts();

        if (productsInCategory != null) {
            List<Product> productsMatchingCriteria = productsInCategory.stream()
                    .filter(product ->
                            product.getDiscountPrice() > 0.00 &&
                                    product.getDiscountPriceEndDate() != null &&
                                    product.getDiscountPriceEndDate().isAfter(currentDateTime) &&
                                    calculateDiscountPercentage(product) >= percentageDiscount)
                    .toList();

            productsOnSale.addAll(productsMatchingCriteria);
        }

        List<Category> subcategories = category.getSubcategories();
        if (subcategories != null) {
            for (Category subcategory : subcategories) {
                addProductsOnSaleFromCategoryWithDiscountPercentage(
                        percentageDiscount,
                        subcategory,
                        productsOnSale,
                        currentDateTime);
            }
        }
    }

    private double calculateDiscountPercentage(Product product) {

        double regularPrice = product.getRegularPrice();
        double discountPrice = product.getDiscountPrice();

        if (regularPrice <= 0.00 || discountPrice <= 0.00) {
            return 0.00;
        }

        return ((regularPrice - discountPrice) / regularPrice) * 100;
    }

    private void applyPercentageDiscountToCategory(
            Category category,
            int percentageDiscount,
            LocalDateTime discountPriceEndDate,
            List<Product> productsToUpdate) {

        for (Product product : category.getProducts()) {
            if (product.getDiscountPrice() >= product.getRegularPrice()) {
                throw new BusinessException("Discount price cannot be greater than or equal to regular price");
            }

            double regularPrice = product.getRegularPrice();
            double discountPrice = regularPrice - (regularPrice * (percentageDiscount / 100.0));
            product.setDiscountPrice(discountPrice);
            product.setDiscountPriceEndDate(discountPriceEndDate);
            if (product.getLowestPrice() > discountPrice) {
                product.setLowestPrice(discountPrice);
            }
            productsToUpdate.add(product);
        }

        for (Category subcategory : category.getSubcategories()) {
            applyPercentageDiscountToCategory(
                    subcategory,
                    percentageDiscount,
                    discountPriceEndDate,
                    productsToUpdate);
        }
    }

    private void collectProductsToClearInCategory(
            Category category,
            List<Product> productsToClear) {

        List<Product> productsInCategory = category.getProducts();

        for (Product product : productsInCategory) {
            if (product.getDiscountPrice() != null && product.getDiscountPrice() > 0.0) {
                productsToClear.add(product);
            }
        }

        List<Category> subcategories = category.getSubcategories();
        for (Category subcategory : subcategories) {
            collectProductsToClearInCategory(subcategory, productsToClear);
        }
    }

}