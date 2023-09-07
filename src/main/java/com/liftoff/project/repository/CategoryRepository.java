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
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds a category by its unique identifier.
     *
     * @param categoryUuId The unique identifier (UUID) of the category to find.
     * @return An {@link Optional} containing the category if found, or an empty {@link Optional} if not found.
     */
    @Query("SELECT c FROM Category c WHERE c.uId = :categoryUuid")
    Optional<Category> findByUId(@Param("categoryUuid") UUID categoryUuId);

    /**
     * Retrieves a list of categories where the parent category is null.
     *
     * @return A list of categories with no parent category.
     */
    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
    List<Category> findByParentCategoryNull();

}
