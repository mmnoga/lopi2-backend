package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.model.Category;
import com.liftoff.project.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class CategoryMapperImplTest {

    @Mock
    private Category category;

    @Mock
    private CategoryService categoryService;

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
        // given
        UUID categoryId = UUID.randomUUID();
        Category subcategory1 = new Category();
        subcategory1.setId(2);
        subcategory1.setName("Subcategory 1");
        subcategory1.setDescription("Subcategory 1 description");

        Category subcategory2 = new Category();
        subcategory2.setId(3);
        subcategory2.setName("Subcategory 2");
        subcategory2.setDescription("Subcategory 2 description");

        Category category = new Category();
        category.setId(1);
        category.setName("Category 1");
        category.setDescription("Category 1");
        category.setUId(categoryId);
        category.setSubcategories(List.of(subcategory1, subcategory2));

        CategoryResponseDTO subcategoryDTO1 = CategoryResponseDTO.builder()
                .uId(subcategory1.getUId())
                .name(subcategory1.getName())
                .description(subcategory1.getDescription())
                .build();

        CategoryResponseDTO subcategoryDTO2 = CategoryResponseDTO.builder()
                .uId(subcategory2.getUId())
                .name(subcategory2.getName())
                .description(subcategory2.getDescription())
                .build();

        CategoryResponseDTO categoryResponseDTO = CategoryResponseDTO.builder()
                .uId(category.getUId())
                .name(category.getName())
                .description(category.getDescription())
                .subcategories(Set.of(subcategoryDTO1, subcategoryDTO2))
                .build();

        doReturn(categoryResponseDTO).when(categoryService).getCategoryByUuId(category.getUId());

        // when
        CategoryResponseDTO responseDTO = categoryMapper.mapEntityToResponse(category);

        // then
        assertEquals(category.getUId(), responseDTO.getUId());
        assertEquals(category.getName(), responseDTO.getName());
        assertEquals(category.getDescription(), responseDTO.getDescription());
        assertEquals(2, responseDTO.getSubcategories().size());
    }

}
