package com.liftoff.project.service.impl;

import com.liftoff.project.controller.product.request.PaginationParameterRequestDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String SORT_TYPE_NAME = "name";
    private static final String SORT_TYPE_REGULAR_PRICE = "regularPrice";
    private static final String PRODUCT_NOT_FOUND_ERROR = "Product with UUID not found: ";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductArchiverService productArchiverService;
    private final StorageService storageService;
    private final ImageAssetRepository imageAssetRepository;

    @Override
    public PaginatedProductResponseDTO getProducts(Pageable pageable) {

        Sort sort = pageable.getSort();

        Sort newSort = sort.and(Sort.by(DEFAULT_SORT_FIELD));

        Pageable customPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                newSort);

        Page<Product> productPage = productRepository.findAll(customPageable);

        List<Product> products = productPage.getContent();
        List<ProductResponseDTO> productResponseList = products.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();

        int totalPages = productPage.getTotalPages();
        long totalProducts = productPage.getTotalElements();
        boolean hasPrevious = productPage.hasPrevious();
        boolean hasNext = productPage.hasNext();

        return new PaginatedProductResponseDTO(
                productResponseList,
                totalPages,
                totalProducts,
                hasPrevious,
                hasNext);
    }

    @Override
    public ProductResponseDTO getProductByUuid(UUID productUuid) {
        Product product = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_ERROR + productUuid));
        return productMapper.mapEntityToResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getNRecentAddedActiveProducts(int n) {
        List<Product> products = productRepository.findTopNRecentActiveProducts(n);

        return products.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();
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
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_ERROR + productUuid));

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
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_ERROR + productUuid));

        return productArchiverService.archiveProduct(
                existingProduct,
                productRequestDTO);
    }

    @Override
    public void deleteProductByUuId(UUID productUuId) {
        Product product = productRepository.findByUId(productUuId)
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_ERROR + productUuId));

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
                .orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND_ERROR + productUuid));

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

        Product existingProduct = getProductEntityByUuid(product.getUId());

        if (product.getStatus().equals(ProductStatus.ACTIVE)) return product;

        existingProduct.setStatus(ProductStatus.ACTIVE);
        existingProduct.setUpdatedAt(Instant.now());

        Product savedProduct = productRepository
                .save(existingProduct);

        ProductResponseDTO updatedProductDTO = productMapper
                .mapEntityToResponse(savedProduct);

        return updatedProductDTO;
    }

    public Product getProductEntityByUuid(UUID productUuid) {
        return productRepository.findByUId(productUuid)
                .orElseThrow(() ->
                        new BusinessException(PRODUCT_NOT_FOUND_ERROR + productUuid));
    }

    @Override
    public Page<ProductResponseDTO> getProductsByCategoryAndSort(
            UUID categoryUuid,
            PaginationParameterRequestDTO paginationParameter) {

        Category category = categoryRepository.findByUId(categoryUuid)
                .orElseThrow(() ->
                        new BusinessException("Category with UUID: " + categoryUuid + " not found."));

        List<Product> products = retrieveProductsFromCategoryAndSubcategories(category);

        Sort.Order sortOrder = paginationParameter.isAscending()
                ? Sort.Order.asc(paginationParameter.getOrderColumn())
                : Sort.Order.desc(paginationParameter.getOrderColumn());

        Pageable pageable = PageRequest.of(
                paginationParameter.getPageIndex(),
                paginationParameter.getPageSize(),
                Sort.by(sortOrder)
        );

        products.sort(productComparator(pageable.getSort()));

        products = products.stream()
                .filter(product -> (paginationParameter.getStatus() == null ||
                        product.getStatus() == paginationParameter.getStatus()))
                .filter(product -> {
                    Boolean isAvailable = paginationParameter.getAvailable();
                    if (isAvailable == null) {
                        return true;
                    }
                    if (isAvailable) {
                        return product.getQuantity() > 0;
                    } else {
                        return product.getQuantity() == 0;
                    }
                })
                .toList();

        int totalProducts = products.size();

        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), totalProducts);

        List<Product> paginatedProducts = new ArrayList<>();

        if (startIndex < endIndex) {
            paginatedProducts = products.subList(startIndex, endIndex);
        }

        List<ProductResponseDTO> productResponseList = paginatedProducts.stream()
                .map(productMapper::mapEntityToResponse)
                .toList();

        return new PageImpl<>(productResponseList, pageable, totalProducts);
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
                .toList();
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

    private Comparator<Product> productComparator(Sort sort) {

        return (product1, product2) -> {
            int compareResult = 0;

            for (Sort.Order order : sort) {
                String property = order.getProperty();

                if (SORT_TYPE_REGULAR_PRICE.equalsIgnoreCase(property)) {
                    compareResult = Double.compare(product1.getRegularPrice(), product2.getRegularPrice());
                } else if (SORT_TYPE_NAME.equalsIgnoreCase(property)) {
                    compareResult = product1.getName().compareTo(product2.getName());
                }

                if (compareResult != 0) {
                    return order.isAscending() ? compareResult : -compareResult;
                }
            }

            return compareResult;
        };
    }

}