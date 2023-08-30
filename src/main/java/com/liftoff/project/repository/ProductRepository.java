package com.liftoff.project.repository;

import com.liftoff.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product by its unique identifier.
     *
     * @param productUuid The unique identifier (UUID) of the product to find.
     * @return An {@link Optional} containing the product if found, or an empty {@link Optional} if not found.
     */
    @Query("SELECT p FROM Product p WHERE p.uId = :productUuid")
    Optional<Product> findByUId(@Param("productUuid") UUID productUuid);

    /**
     * Finds the top N recently added active products.
     *
     * @param n The number of products to retrieve.
     * @return A list of active products that were recently added, limited to the specified number.
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' " +
            "AND p.createdAt IS NOT NULL " +
            "ORDER BY p.createdAt DESC LIMIT :n")
    List<Product> findTopNRecentActiveProducts(@Param("n") int n);

}
