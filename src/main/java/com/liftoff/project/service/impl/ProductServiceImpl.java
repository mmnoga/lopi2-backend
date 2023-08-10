package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.CategoryNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.service.CategoryService;
import com.liftoff.project.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
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

    @Override
    public PaginatedProductResponseDTO getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
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
                .orElseThrow(() -> new ProductNotFoundException("Product with UUID " + productUuid + " not found."));
        return productMapper.mapEntityToResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryId(UUID categoryId) {
        Category category = categoryRepository.findByUId(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with UUID " + categoryId + " not found."));

        List<Product> products = category.getProducts()
                .stream().toList();
        return products.stream()
                .map(productMapper::mapEntityToResponse)
                .collect(Collectors.toList());
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

        if (productRequestDTO.getCategories() != null) {
            Set<Category> existingCategories = categoryService.getExistingCategories(productRequestDTO.getCategories());
            newProduct.setCategories(existingCategories);
        }

        Product savedProduct = productRepository.save(newProduct);
        return productMapper.mapEntityToResponse(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProductByUuid(UUID productUuid, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findByUId(productUuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with UUID " + productUuid + " not found"));

        updateProductFromRequest(existingProduct, productRequestDTO);
        
         Set<Category> existingCategories =
                    categoryService.getExistingCategories(productRequestDTO.getCategories());
      
        if (productRequestDTO.getCategories() != null) {
            existingProduct.setCategories(existingCategories);
        }
      
        return productArchiverService.archiveProduct(existingProduct, productRequestDTO, existingCategories);
    }

    @Override
    public void deleteProductByUuId(UUID productUuId) {
        Product product = productRepository.findByUId(productUuId)
                .orElseThrow(() -> new ProductNotFoundException("Product with UUID " + productUuId + " not found."));

        if (product.getStatus() == ProductStatus.IN_PREPARATION) {
            productRepository.delete(product);
        } else if (product.getStatus() == ProductStatus.ACTIVE) {
            product.setStatus(ProductStatus.CLOSED);
            product.setArchivedAt(Instant.now());
            productRepository.save(product);
        }
    }

    @Override
    public ProductResponseDTO activateProduct(ProductResponseDTO product) {
        if (product.getStatus().equals(ProductStatus.ACTIVE)) return product;

        product.setStatus(ProductStatus.ACTIVE);
        product.setUpdatedAt(Instant.now());

        return product;
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
  
  }
