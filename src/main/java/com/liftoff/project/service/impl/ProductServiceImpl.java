package com.liftoff.project.service.impl;

import com.liftoff.project.controller.product.request.ProductRequestDTO;
import com.liftoff.project.controller.product.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.product.response.ProductResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.exception.TechnicalException;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.ImageAsset;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.repository.ImageAssetRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.service.CategoryService;
import com.liftoff.project.service.ProductService;
import com.liftoff.project.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductArchiverService productArchiverService;
    private final StorageService storageService;
    private final ImageAssetRepository imageAssetRepository;

    @Override
    public PaginatedProductResponseDTO getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product> products = productPage.getContent();
        List<ProductResponseDTO> productResponseList = products.stream()
                .map(productMapper::mapEntityToResponse)
                .collect(Collectors.toList());

        int totalPages = productPage.getTotalPages();
        long totalProducts = productPage.getTotalElements();

        return new PaginatedProductResponseDTO(productResponseList, totalPages, totalProducts);
    }

    @Override
    public ProductResponseDTO getProductByUuid(UUID productUuid) {
        Product product = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found."));
        return productMapper.mapEntityToResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getNRecentAddedActiveProducts(int n) {
        List<Product> products = productRepository.findTopNRecentActiveProducts(n);

        return products.stream()
                .map(productMapper::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product newProduct = productMapper.mapRequestToEntity(productRequestDTO);

        if (productRequestDTO.getStatus() == null) {
            newProduct.setStatus(ProductStatus.ACTIVE);
        }

        if (productRequestDTO.getCategories() != null) {
            List<Category> existingCategories =
                    categoryService
                            .getExistingCategories(productRequestDTO.getCategories());
            newProduct.setCategories(existingCategories);
        }

        Product savedProduct = productRepository.save(newProduct);

        return productMapper.mapEntityToResponse(savedProduct);
    }

    @Override
    public ProductResponseDTO addImageToProduct(UUID productUuid, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found."));

        String imageUrl = storageService.uploadFile(imageFile);

        ImageAsset imageAsset = new ImageAsset();
        imageAsset.setAssetUrl(imageUrl);
        imageAssetRepository.save(imageAsset);

        product.getImages().add(imageAsset);

        Product savedProduct = productRepository.save(product);

        return productMapper.mapEntityToResponse(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProductByUuid(UUID productUuid, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found"));

        updateProductFromRequest(existingProduct, productRequestDTO);

        List<Category> existingCategories = new ArrayList<>();

        if (productRequestDTO.getCategories() != null) {
            existingCategories = categoryService
                    .getExistingCategories(
                            productRequestDTO.getCategories());
            existingProduct.setCategories(existingCategories);
        }

        return productArchiverService.archiveProduct(
                existingProduct,
                productRequestDTO,
                existingCategories);
    }

    @Override
    public void deleteProductByUuId(UUID productUuId) {
        Product product = productRepository.findByUId(productUuId)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuId + " not found."));

        if (product.getStatus() == ProductStatus.IN_PREPARATION) {
            productRepository.delete(product);
        } else if (product.getStatus() == ProductStatus.ACTIVE) {
            product.setStatus(ProductStatus.CLOSED);
            product.setArchivedAt(Instant.now());
            productRepository.save(product);
        }
    }

    public ProductResponseDTO deleteImageByUrlFromProduct(UUID productUuid, String imageUrl) {
        Product product = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException("Product with UUID: " + productUuid + " not found."));

        List<ImageAsset> images = product.getImages();
        boolean imageRemoved = images.removeIf(
                image ->
                        image.getAssetUrl().equals(imageUrl));
        if (!imageRemoved) {
            throw new TechnicalException("Image with URL: " + imageUrl + " not found in the product.");
        }

        productRepository.save(product);

        return productMapper.mapEntityToResponse(product);
    }

    @Override
    public ProductResponseDTO activateProduct(ProductResponseDTO product) {
        if (product.getStatus().equals(ProductStatus.ACTIVE)) return product;

        product.setStatus(ProductStatus.ACTIVE);
        product.setUpdatedAt(Instant.now());

        return product;
    }

    public Product getProductEntityByUuid(UUID productUuid) {
        return productRepository.findByUId(productUuid)
                .orElseThrow(() ->
                        new BusinessException("Product not found with UUID: " + productUuid));
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryUuid(UUID categoryUuid) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() ->
                        new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> products =
                retrieveProductsFromCategoryAndSubcategories(category);

        return products.stream()
                .map(productMapper::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    private void updateProductFromRequest(Product product, ProductRequestDTO req) {
        if (req.getName() != null) product.setName(req.getName());
        if (req.getSku() != null) product.setSku(req.getSku());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
        if (req.getRegularPrice() != null) product.setRegularPrice(req.getRegularPrice());
        if (req.getDiscountPrice() != null) product.setDiscountPrice(req.getDiscountPrice());
        if (req.getDiscountPriceEndDate() != null) product.setDiscountPriceEndDate(req.getDiscountPriceEndDate());
        if (req.getLowestPrice() != null) product.setLowestPrice(req.getLowestPrice());
        if (req.getShortDescription() != null) product.setShortDescription(req.getShortDescription());
        if (req.getNote() != null) product.setNote(req.getNote());
        if (req.getProductscol() != null) product.setProductscol(req.getProductscol());
        if (req.getQuantity() != null) product.setQuantity(req.getQuantity());
    }

    private List<Product> retrieveProductsFromCategoryAndSubcategories(
            Category category) {

        List<Product> productsInCategory = category.getProducts();
        List<Product> products = new ArrayList<>(productsInCategory);

        List<Category> subcategories = category.getSubcategories();
        for (Category subcategory : subcategories) {
            List<Product> productsInSubcategory =
                    retrieveProductsFromCategoryAndSubcategories(subcategory);
            products.addAll(productsInSubcategory);
        }

        return products;
    }

}