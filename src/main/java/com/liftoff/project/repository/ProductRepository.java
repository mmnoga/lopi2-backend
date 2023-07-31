package com.liftoff.project.repository;

import com.liftoff.project.model.Category;
import com.liftoff.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.uId = :productUuid")
    Optional<Product> findByUId(@Param("productUuid") UUID productUuid);

    List<Product> findByCategories(Category category);

    List<Product> findByCategoriesContaining(Category category);

}
