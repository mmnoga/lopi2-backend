package com.liftoff.project.repository;

import com.liftoff.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.uId = :categoryUuid")
    Optional<Category> findByUId(@Param("categoryUuid") UUID categoryUuId);

    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
    List<Category> findByParentCategoryNull();

}
