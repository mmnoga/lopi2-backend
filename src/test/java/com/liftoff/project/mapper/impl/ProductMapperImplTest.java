package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.mapper.CategoryMapper;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductMapperImplTest {

    private ProductMapperImpl productMapper;
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = mock(CategoryMapper.class);
        productMapper = new ProductMapperImpl();
    }

    @Test
    void shouldReturnProductDTOWithoutCategoriesForProductWithoutCategories() {
        // Given
        Product product = new Product();
        product.setUId(UUID.randomUUID());
        product.setName("Product 1");
        product.setSku("SKU-001");
        product.setRegularPrice(100.0);
        product.setDiscountPrice(90.0);
        product.setLowestPrice(80.0);
        product.setDescription("Description 1");
        product.setShortDescription("Short Description 1");
        product.setNote("Note 1");
        product.setStatus(ProductStatus.ACTIVE);
        product.setProductscol("Products Col 1");
        product.setQuantity(10);

        // When
        ProductResponseDTO responseDTO = productMapper.mapEntityToResponse(product);

        // Then
        assertEquals(product.getUId(), responseDTO.getUId());
        assertEquals(product.getName(), responseDTO.getName());
        assertEquals(product.getSku(), responseDTO.getSku());
        assertEquals(product.getRegularPrice(), responseDTO.getRegularPrice());
        assertEquals(product.getDiscountPrice(), responseDTO.getDiscountPrice());
        assertEquals(product.getLowestPrice(), responseDTO.getLowestPrice());
        assertEquals(product.getDescription(), responseDTO.getDescription());
        assertEquals(product.getShortDescription(), responseDTO.getShortDescription());
        assertEquals(product.getNote(), responseDTO.getNote());
        assertEquals(product.getStatus(), responseDTO.getStatus());
        assertEquals(product.getProductscol(), responseDTO.getProductscol());
        assertEquals(product.getQuantity(), responseDTO.getQuantity());
        assertEquals(Collections.emptyList(), responseDTO.getCategories());
    }

    @Test
    void shouldReturnProductDTOWithCategoriesForProductWithCategories() {
        //
        UUID cat1UuId = UUID.randomUUID();
        Category category1 = new Category();
        category1.setUId(cat1UuId);
        category1.setName("Category 1");
        category1.setDescription("Category 1 description");

        UUID cat2UuId = UUID.randomUUID();
        Category category2 = new Category();
        category2.setUId(cat2UuId);
        category2.setName("Category 2");
        category2.setDescription("Category 2 description");

        List<Category> categories = List.of(category1, category2);

        Product product = new Product();
        product.setCategories(categories);

        CategoryResponseDTO categoryResponse1 =
                new CategoryResponseDTO(cat1UuId, "Category 1", "Category 1 description");
        CategoryResponseDTO categoryResponse2 =
                new CategoryResponseDTO(cat2UuId, "Category 2", "Category 2 description");

        when(categoryMapper.mapEntityToResponse(category1)).thenReturn(categoryResponse1);
        when(categoryMapper.mapEntityToResponse(category2)).thenReturn(categoryResponse2);

        // When
        ProductResponseDTO responseDTO = productMapper.mapEntityToResponse(product);

        // Then
        assertEquals(categories.size(), responseDTO.getCategories().size());
    }

    @Test
    void shouldReturnProductEntityForProductDTO() {
        // Given
        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("Product 1")
                .sku("SKU")
                .regularPrice(100.0)
                .discountPrice(90.0)
                .lowestPrice(80.0)
                .description("Description 1")
                .shortDescription("Short description 1")
                .note("Note 1")
                .status(ProductStatus.ACTIVE)
                .productscol("Products Col 1")
                .quantity(10)
                .build();

        // When
        Product product = productMapper.mapRequestToEntity(productRequestDTO);

        // Then
        assertEquals(productRequestDTO.getName(), product.getName());
        assertEquals(productRequestDTO.getSku(), product.getSku());
        assertEquals(productRequestDTO.getRegularPrice(), product.getRegularPrice());
        assertEquals(productRequestDTO.getDiscountPrice(), product.getDiscountPrice());
        assertEquals(productRequestDTO.getLowestPrice(), product.getLowestPrice());
        assertEquals(productRequestDTO.getDescription(), product.getDescription());
        assertEquals(productRequestDTO.getShortDescription(), product.getShortDescription());
        assertEquals(productRequestDTO.getNote(), product.getNote());
        assertEquals(productRequestDTO.getStatus(), product.getStatus());
        assertEquals(productRequestDTO.getProductscol(), product.getProductscol());
        assertEquals(productRequestDTO.getQuantity(), product.getQuantity());
    }

}