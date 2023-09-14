package com.liftoff.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftoff.project.Lopi2Application;
import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.controller.response.PaginatedProductResponseDTO;
import com.liftoff.project.controller.response.ProductResponseDTO;
import com.liftoff.project.exception.category.CategoryNotFoundException;
import com.liftoff.project.exception.product.ProductNotFoundException;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.service.ProductService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldReturnPaginatedProducts() throws Exception {
        // given
        int page = 0;
        int size = 2;

        ProductResponseDTO product1 = ProductResponseDTO.builder()
                .name("Product 1")
                .description("Product 1 description")
                .regularPrice(10.99)
                .build();
        ProductResponseDTO product2 = ProductResponseDTO.builder()
                .name("Product 2")
                .description("Product 2 description")
                .regularPrice(20.49)
                .build();

        List<ProductResponseDTO> products = Arrays.asList(product1, product2);
        PaginatedProductResponseDTO paginatedProducts = new PaginatedProductResponseDTO(products, 1, 2);

        // when
        when(productService.getProducts(any(Pageable.class))).thenReturn(paginatedProducts);

        // then
        mockMvc.perform(get("/api/products")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].name").value("Product 1"))
                .andExpect(jsonPath("$.products[0].description").value("Product 1 description"))
                .andExpect(jsonPath("$.products[0].regularPrice").value(10.99))
                .andExpect(jsonPath("$.products[1].name").value("Product 2"))
                .andExpect(jsonPath("$.products[1].description").value("Product 2 description"))
                .andExpect(jsonPath("$.products[1].regularPrice").value(20.49));
    }

    @Test
    public void shouldReturnNoContentIfNoProductsAdded() throws Exception {
        // given
        int page = 0;
        int size = 10;
        List<ProductResponseDTO> emptyProductsList = Collections.emptyList();
        PaginatedProductResponseDTO paginatedProducts = new PaginatedProductResponseDTO(emptyProductsList, 0, 0);

        // when
        when(productService.getProducts(any(Pageable.class))).thenReturn(paginatedProducts);

        // then
        mockMvc.perform(get("/api/products")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnProductByUuid() throws Exception {
        // given
        UUID existingProductUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        ProductResponseDTO existingProductResponse = ProductResponseDTO.builder()
                .name("Product 1")
                .description("Product 1 description")
                .regularPrice(10.99)
                .build();

        // when
        when(productService.getProductByUuid(existingProductUuid)).thenReturn(existingProductResponse);

        // then
        mockMvc.perform(get("/api/products/{productUuid}", existingProductUuid.toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Product 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Product 1 description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.regularPrice", Matchers.is(10.99)));
    }

    @Test
    void shouldReturnNotFoundWhenProductByUuidNotExist() throws Exception {
        // given
        UUID nonExistingProductUuid = UUID.fromString("f28bd377-3a7d-44fe-bbc9-adeb3bea03fa");

        // when
        when(productService.getProductByUuid(nonExistingProductUuid)).thenReturn(null);

        // then
        mockMvc.perform(get("/api/products/{productUuid}",
                        nonExistingProductUuid.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddNewProduct() throws Exception {
        // given
        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("New Product")
                .description("New Product description")
                .regularPrice(19.99)
                .build();

        ProductResponseDTO createdProduct = ProductResponseDTO.builder()
                .name("New Product")
                .description("New Product description")
                .regularPrice(19.99)
                .build();

        // when
        when(productService.addProduct(any(ProductRequestDTO.class))).thenReturn(createdProduct);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("New Product")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("New Product description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.regularPrice", Matchers.is(19.99)));
    }

    @Test
    void shouldDeleteProductWithGivenUuid() throws Exception {
        // given
        UUID productUuid = UUID.randomUUID();

        // when
        Mockito.doNothing().when(productService).deleteProductByUuId(eq(productUuid));

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{productUuid}", productUuid))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnProductNotFoundExceptionWhenUuidNotExist() throws Exception {
        // given
        UUID productUuid = UUID.randomUUID();

        // when
        Mockito.doThrow(new ProductNotFoundException("Product with UUID " + productUuid + " not found."))
                .when(productService).deleteProductByUuId(eq(productUuid));

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{productUuid}", productUuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExistingProduct() throws Exception {
        // given
        UUID productUuid = UUID.randomUUID();
        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("Updated Product")
                .description("Updated Product Description")
                .build();

        ProductResponseDTO updatedProductResponse = ProductResponseDTO.builder()
                .uId(productUuid)
                .name("Updated Product")
                .description("Updated Product Description")
                .build();

        // when
        when(productService.updateProductByUuid(eq(productUuid), any(ProductRequestDTO.class)))
                .thenReturn(updatedProductResponse);

        // then
        mockMvc.perform(put("/api/products/{productUuid}", productUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uid").value(productUuid.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Product Description"));
    }

    @Test
    void shouldReturnProductNotFoundExceptionWhenUuidNotExistUpdatedProduct() throws Exception {
        // given
        UUID productUuid = UUID.randomUUID();
        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("Updated Product")
                .description("Updated Product Description")
                .build();

        // when
        when(productService.updateProductByUuid(eq(productUuid), any(ProductRequestDTO.class)))
                .thenThrow(new ProductNotFoundException("Product with UUID " + productUuid + " not found."));

        // then
        mockMvc.perform(put("/api/products/{productUuid}", productUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnProductsByCategoryId() throws Exception {
        // given
        UUID categoryId = UUID.fromString("f28bd377-3a7d-44fe-bbc9-adeb3bea03fa");

        Product product1 = new Product();
        product1.setUId(UUID.randomUUID());
        product1.setName("Product 1");
        product1.setDescription("Product 1 description");

        Product product2 = new Product();
        product2.setUId(UUID.randomUUID());
        product2.setName("Product 2");
        product2.setDescription("Product 2 description");

        ProductResponseDTO productResponseDTO1 = ProductResponseDTO.builder()
                .uId(product1.getUId())
                .name(product1.getName())
                .description(product1.getDescription())
                .build();
        ProductResponseDTO productResponseDTO2 = ProductResponseDTO.builder()
                .uId(product2.getUId())
                .name(product2.getName())
                .description(product2.getDescription())
                .build();

        List<ProductResponseDTO> expectedResponse = Arrays.asList(productResponseDTO1, productResponseDTO2);

        when(productService.getProductsByCategoryId(categoryId)).thenReturn(expectedResponse);

        // when & then
        mockMvc.perform(get("/api/products/by-category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uid").value(product1.getUId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(product1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(product1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].uid").value(product2.getUId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(product2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(product2.getDescription()));
    }

    @Test
    void shouldReturnNoContentWhenNoProductsFoundByCategoryId() throws Exception {
        // given
        UUID categoryId = UUID.fromString("f28bd377-3a7d-44fe-bbc9-adeb3bea03fa");

        when(productService.getProductsByCategoryId(categoryId)).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/products/by-category/{categoryId}", categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenCategoryNotFoundByCategoryId() throws Exception {
        // given
        UUID categoryId = UUID.fromString("f28bd377-3a7d-44fe-bbc9-adeb3bea03fa");

        when(productService.getProductsByCategoryId(categoryId))
                .thenThrow(new CategoryNotFoundException("Category with UUID " + categoryId + " not found."));

        // when & then
        mockMvc.perform(get("/api/products/by-category/{categoryId}", categoryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnProductsWhenValidN() throws Exception {
        // given
        int n = 3;
        List<ProductResponseDTO> dummyProducts = createNDummyProducts(n);
        when(productService.getNRecentAddedActiveProducts(anyInt())).thenReturn(dummyProducts);

        // when & then
        mockMvc.perform(get("/api/products/recent-added")
                        .param("n", String.valueOf(n)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnEmptyListOfProductsWhenNoProductAdded() throws Exception {
        // given
        int n = 5;
        List<ProductResponseDTO> emptyProductsList = Collections.emptyList();

        // when
        when(productService.getNRecentAddedActiveProducts(n)).thenReturn(emptyProductsList);

        // then
        mockMvc.perform(get("/api/products/recent-added")
                        .param("n", String.valueOf(n)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldArchiveActiveProduct() throws Exception {
        // given
        UUID existingActiveProductUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");

        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("Updated name")
                .description("Updated description")
                .build();

        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .uId(existingActiveProductUuid)
                .name("Updated name")
                .description("Updated description")
                .status(ProductStatus.ACTIVE)
                .archivedAt(Instant.now())
                .build();

        when(productService.updateProductByUuid(eq(existingActiveProductUuid), any(ProductRequestDTO.class)))
                .thenReturn(productResponseDTO);

        // when & then
        mockMvc.perform(put("/api/products/{uuid}", existingActiveProductUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.archivedAt").value(notNullValue()));
    }

    @Test
    void shouldArchiveClosedProduct() throws Exception {
        // given
        UUID existingClosedProductUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a97");

        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("Updated name")
                .description("Updated description")
                .build();

        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .uId(existingClosedProductUuid)
                .name("Updated name")
                .description("Updated description")
                .status(ProductStatus.IN_PREPARATION)
                .build();

        when(productService.updateProductByUuid(eq(existingClosedProductUuid), any(ProductRequestDTO.class)))
                .thenReturn(productResponseDTO);

        // when & then
        mockMvc.perform(put("/api/products/{uuid}", existingClosedProductUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PREPARATION"));
    }

    @Test
    void shouldArchiveInPreparationProduct() throws Exception {
        // given
        UUID existingInPreparationProductUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a98");

        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .name("Updated name")
                .description("Updated description")
                .build();

        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .uId(existingInPreparationProductUuid)
                .name("Updated name")
                .description("Updated description")
                .status(ProductStatus.IN_PREPARATION)
                .build();

        when(productService.updateProductByUuid(eq(existingInPreparationProductUuid), any(ProductRequestDTO.class)))
                .thenReturn(productResponseDTO);

        // when & then
        mockMvc.perform(put("/api/products/{uuid}", existingInPreparationProductUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PREPARATION"));
    }

    @Test
    void shouldAddImageToProduct() throws Exception {
        // given
        UUID productUuid = UUID.fromString("e3bb11fc-d45d-4d78-b72c-21d41f494a96");
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Image Content".getBytes(StandardCharsets.UTF_8)
        );

        ProductResponseDTO updatedProductResponse = ProductResponseDTO.builder()
                .uId(productUuid)
                .name("Product 1")
                .description("Product 1 description")
                .regularPrice(10.99)
                .build();

        when(productService.addImageToProduct(eq(productUuid), any(MultipartFile.class)))
                .thenReturn(updatedProductResponse);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products/{productUuid}/images", productUuid)
                        .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(productUuid.toString()))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.description").value("Product 1 description"))
                .andExpect(jsonPath("$.regularPrice").value(10.99));
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private List<ProductResponseDTO> createNDummyProducts(int n) {
        List<ProductResponseDTO> dummyProducts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dummyProducts.add(ProductResponseDTO.builder()
                    .name("Product " + (i + 1))
                    .description("Product " + (i + 1) + " description")
                    .regularPrice(2.99 + i)
                    .status(ProductStatus.ACTIVE)
                    .build());
        }
        return dummyProducts;
    }

}