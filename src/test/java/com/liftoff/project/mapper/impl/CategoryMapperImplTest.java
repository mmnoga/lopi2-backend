package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.category.request.CategoryUidRequestDTO;
import com.liftoff.project.controller.category.response.CategoryResponseDTO;
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
    void shouldReturnNullObjectForNullCategory() {
        // Given
        Category category = null;

        // When
        CategoryResponseDTO responseDTO = categoryMapper.mapEntityToResponse(category);

        // Then
        assertNull(responseDTO);
    }

    @Test
    void shouldReturnCategoryDTOForCategoryWithoutSubcategories() {
        // Given
        when(category.getId()).thenReturn(1L);
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
    void shouldReturnCategoryWithSubcategoriesDTOForCategoryWithSubcategories() {
        // given
        UUID categoryId = UUID.randomUUID();
        Category subcategory1 = new Category();
        subcategory1.setId(2L);
        subcategory1.setName("Subcategory 1");
        subcategory1.setDescription("Subcategory 1 description");

        Category subcategory2 = new Category();
        subcategory2.setId(3L);
        subcategory2.setName("Subcategory 2");
        subcategory2.setDescription("Subcategory 2 description");

        Category category = new Category();
        category.setId(1L);
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

    @Test
    void shouldMapCategoryUidRequestToEntity() {
        // given
        UUID categoryUid = UUID.randomUUID();
        CategoryUidRequestDTO requestDTO = new CategoryUidRequestDTO(categoryUid);

        // when
        Category category = categoryMapper.mapUidRequestToEntity(requestDTO);

        // then
        assertEquals(categoryUid, category.getUId());
    }

    @Test
    void shouldReturnNullEntityForNullCategoryUidRequest() {
        // given
        CategoryUidRequestDTO requestDTO = null;

        // when
        Category category = categoryMapper.mapUidRequestToEntity(requestDTO);

        // then
        assertNull(category);
    }

}
