package com.liftoff.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.controller.request.CategoryRequestDTO;
import com.liftoff.project.controller.response.CategoryResponseDTO;
import com.liftoff.project.exception.category.CategoryNotFoundException;
import com.liftoff.project.exception.category.InvalidParentCategoryException;
import com.liftoff.project.model.Category;
import com.liftoff.project.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void shouldReturnAllCategories() throws Exception {
        // given
        CategoryResponseDTO category1 = CategoryResponseDTO.builder()
                .name("Category 1")
                .description("Category 1 description")
                .build();
        CategoryResponseDTO category2 = CategoryResponseDTO.builder()
                .name("Category 2")
                .description("Category 2 description")
                .build();

        List<CategoryResponseDTO> categories = Arrays.asList(category1, category2);

        // when
        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Category 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Category 2")));
    }

    @Test
    void shouldReturnCategoryByUuid() throws Exception {
        // given
        UUID existingCategoryUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        CategoryResponseDTO existingCategoryResponse = CategoryResponseDTO.builder()
                .name("Category 1")
                .description("Category 1 description")
                .build();

        // when
        Mockito.when(categoryService.getCategoryByUuId(existingCategoryUuid)).thenReturn(existingCategoryResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{categoryUuId}", existingCategoryUuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Category 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Category 1 description")));
    }

    @Test
    void shouldAddNewCategory() throws Exception {
        // given
        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("New Category")
                .description("New Category description")
                .build();

        CategoryResponseDTO createdCategory = CategoryResponseDTO.builder()
                .name("New Category")
                .description("New Category description")
                .build();

        // when
        Mockito.when(categoryService.addCategory(Mockito.any(CategoryRequestDTO.class))).thenReturn(createdCategory);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("New Category")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("New Category description")));
    }

    @Test
    void shouldAddSubcategoryToCategory() throws Exception {
        // given
        Category parentCategory = new Category();
        parentCategory.setName("Parent Category");
        parentCategory.setDescription("Parent Category description");
        parentCategory.setUId(UUID.randomUUID());

        CategoryRequestDTO subcategoryRequestDTO = CategoryRequestDTO.builder()
                .name("Subcategory")
                .description("Subcategory description")
                .build();

        CategoryResponseDTO subcategoryResponseDTO = CategoryResponseDTO.builder()
                .name("Subcategory")
                .description("Subcategory description")
                .build();

        // when
        Mockito.when(categoryService.addCategory(Mockito.any())).thenReturn(subcategoryResponseDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(subcategoryRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Subcategory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Subcategory description")));

        Mockito.verify(categoryService, Mockito.times(1)).addCategory(Mockito.any());
    }

    @Test
    void shouldDeleteCategoryWithGivenUuid() throws Exception {
        // given
        UUID categoryUuid = UUID.randomUUID();
        String successMessage = "Category with UUID " + categoryUuid + " has been successfully deleted.";

        // when
        Mockito.doNothing().when(categoryService).deleteCategoryByUuid(Mockito.any(UUID.class));

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/{categoryUuid}", categoryUuid))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    void shouldReturnCategoryNotFoundExceptionWhenUuIdNotExist() throws Exception {
        // given
        UUID categoryUuid = UUID.fromString("f28bd377-3a7d-44fe-bbc9-adeb3bea03fa");
        String expectedErrorMessage = "Category with UUID " + categoryUuid + " not found.";

        // when
        Mockito.when(categoryService.getCategoryByUuId(Mockito.eq(categoryUuid)))
                .thenThrow(new CategoryNotFoundException(expectedErrorMessage));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/" + categoryUuid))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void shouldUpdateCategoryByUuid() throws Exception {
        // given
        UUID existingCategoryUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("Updated Category")
                .description("Updated Category description")
                .build();

        CategoryResponseDTO updatedCategoryResponse = CategoryResponseDTO.builder()
                .name("Updated Category")
                .description("Updated Category description")
                .build();

        // when
        Mockito.when(categoryService.updateCategory(Mockito.eq(existingCategoryUuid), Mockito.any(CategoryRequestDTO.class)))
                .thenReturn(updatedCategoryResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/{categoryUuid}", existingCategoryUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                        Matchers.is("Updated Category")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        Matchers.is("Updated Category description")));
    }

    @Test
    public void shouldHandleInvalidParentCategoryException() throws Exception {
        // given
        UUID categoryUuid = UUID.randomUUID();

        CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder()
                .name("Category with Invalid Parent")
                .description("Invalid Parent Category Description")
                .parentCategoryId(categoryUuid)
                .build();

        String errorMessage = "Category cannot be its own parent.";

        Mockito.when(categoryService.updateCategory(Mockito.eq(categoryUuid), Mockito.any(CategoryRequestDTO.class)))
                .thenThrow(new InvalidParentCategoryException(errorMessage));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/{categoryUuid}", categoryUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnProductQuantityInCategory() throws Exception {
        // given
        UUID categoryUuid = UUID.randomUUID();
        int expectedProductQuantity = 10;

        Mockito.when(categoryService.getProductQuantityInCategory(Mockito.eq(categoryUuid)))
                .thenReturn(expectedProductQuantity);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(
                                "/api/categories/{categoryUuId}/product-quantity",
                                categoryUuid
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(expectedProductQuantity)));
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Map.of("message", obj));
    }

}