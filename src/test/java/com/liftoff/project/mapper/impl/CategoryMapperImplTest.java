package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class CategoryMapperImplTest {

    @Mock
    private Category category;

    @InjectMocks
    private CategoryMapperImpl categoryMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnNullObjectForNullCategory() {
        // Given
        Category category = null;

        // When
        CategoryResponseDTO responseDTO = categoryMapper.mapEntityToResponse(category);

        // Then
        assertNull(responseDTO);
    }

    @Test
    public void shouldReturnCategoryDTOForCategoryWithoutSubcategories() {
        // Given
        when(category.getId()).thenReturn(1);
        when(category.getName()).thenReturn("Category 1");
        when(category.getDescription()).thenReturn("Description 1");

        // When
        CategoryResponseDTO responseDTO = categoryMapper.mapEntityToResponse(category);

        // Then
        assertEquals(category.getName(), responseDTO.getName());
        assertEquals(category.getDescription(), responseDTO.getDescription());
        assertEquals(0, responseDTO.getSubcategories().size());
    }

    @Test
    public void shouldReturnCategoryWithSubcategoriesDTOForCategoryWithSubcategories() {
        // Given
        Category subcategory = new Category();
        subcategory.setName("Subcategory 1");
        subcategory.setDescription("Subcategory Description 1");

        List<Category> subcategories = Collections.singletonList(subcategory);

        when(category.getId()).thenReturn(1);
        when(category.getName()).thenReturn("Category 1");
        when(category.getDescription()).thenReturn("Description 1");
        when(category.getSubcategories()).thenReturn(subcategories);

        // When
        CategoryResponseDTO responseDTO = categoryMapper.mapEntityToResponse(category);

        // Then
        assertEquals(category.getName(), responseDTO.getName());
        assertEquals(category.getDescription(), responseDTO.getDescription());
        assertEquals(subcategories.size(), responseDTO.getSubcategories().size());

        CategoryResponseDTO subcategoryResponseDTO = responseDTO.getSubcategories().iterator().next();
        assertEquals(subcategory.getName(), subcategoryResponseDTO.getName());
        assertEquals(subcategory.getDescription(), subcategoryResponseDTO.getDescription());
    }

}