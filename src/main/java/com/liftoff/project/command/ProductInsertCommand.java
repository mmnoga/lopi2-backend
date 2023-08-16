package com.liftoff.project.command;

import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.repository.ProductRepository;
import jakarta.annotation.Priority;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Priority(1)
@AllArgsConstructor
public class ProductInsertCommand implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        Category cat1 = Category.builder()
                .uId(UUID.randomUUID())
                .name("Category 1")
                .description("Category 1 descriptions")
                .build();
        Category cat2 = Category.builder()
                .uId(UUID.randomUUID())
                .name("Category 2")
                .description("Category 2 descriptions")
                .build();
        Category cat3 = Category.builder()
                .uId(UUID.randomUUID())
                .name("Category 3")
                .description("Category 2 descriptions")
                .parentCategory(cat2)
                .build();
        Category cat4 = Category.builder()
                .uId(UUID.randomUUID())
                .name("Category 4")
                .description("Category 4 descriptions")
                .parentCategory(cat3)
                .build();
        Category cat5 = Category.builder()
                .uId(UUID.randomUUID())
                .name("Category 5")
                .description("Category 5 descriptions")
                .parentCategory(cat2)
                .build();
        Category cat6 = Category.builder()
                .uId(UUID.randomUUID())
                .name("Category 6")
                .description("Category 6 descriptions")
                .parentCategory(cat5)
                .build();

        categoryRepository.saveAll(List.of(cat1, cat2, cat3, cat4, cat5, cat6));

        List<Product> productList = IntStream.range(1, 51)
                .mapToObj(i -> Product.builder()
                        .uId(UUID.randomUUID())
                        .name("Product " + i)
                        .description("Product " + i + " description")
                        .status(ProductStatus.ACTIVE)
                        .categories(null)
                        .build())
                .collect(Collectors.toList());

        productRepository.saveAll(productList);

    }

}
