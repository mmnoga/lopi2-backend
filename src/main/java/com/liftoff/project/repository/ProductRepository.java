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

    @Query("SELECT p FROM Product p WHERE p.uId = :productUuid")
    Optional<Product> findByUId(@Param("productUuid") UUID productUuid);

    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' " +
            "AND p.createdAt IS NOT NULL " +
            "ORDER BY p.createdAt DESC LIMIT :n")
    List<Product> findTopNRecentActiveProducts(@Param("n") int n);

}
