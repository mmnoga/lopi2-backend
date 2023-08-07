package com.liftoff.project.service.impl;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.CategoryNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.service.CategoryService;
import com.liftoff.project.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public PaginatedProductResponseDTO getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product> products = productPage.getContent();
        List<ProductResponseDTO> productResponseList = products.stream()
                .map(productMapper::mapEntityToResponse)
                .collect(Collectors.toList());

        int totalPages = productPage.getTotalPages();

        return new PaginatedProductResponseDTO(productResponseList, totalPages);
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

        Set<Category> existingCategories = categoryService.getExistingCategories(productRequestDTO.getCategories());
        newProduct.setCategories(existingCategories);

        Product savedProduct = productRepository.save(newProduct);
        return productMapper.mapEntityToResponse(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProductByUuid(UUID productUuid, ProductRequestDTO productRequestDTO) {
        Optional<Product> productOptional = productRepository.findByUId(productUuid);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            updateProductFromRequest(existingProduct, productRequestDTO);

            Set<Category> existingCategories = categoryService.getExistingCategories(productRequestDTO.getCategories());
            existingProduct.setCategories(existingCategories);

            Product updatedProduct = productRepository.save(existingProduct);
            return productMapper.mapEntityToResponse(updatedProduct);
        } else {
            String errorMessage = "Product with UUID " + productUuid + " not found.";
            throw new ProductNotFoundException(errorMessage);
        }
    }

    @Override
    public void deleteProductByUuId(UUID productUuId) {
        Product product = productRepository.findByUId(productUuId)
                .orElseThrow(() -> new ProductNotFoundException("Product with UUID " + productUuId + " not found."));
        productRepository.delete(product);
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
        product.setPublished(productRequestDTO.getPublished());
        product.setProductscol(productRequestDTO.getProductscol());
        product.setQuantity(productRequestDTO.getQuantity());
    }

}
