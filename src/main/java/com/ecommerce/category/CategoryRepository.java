package com.ecommerce.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySlug(String slug);
    Optional<Category> findBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.parentId IS NULL AND c.isActive = true ORDER BY c.id ASC")
    List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parentId IS NULL ORDER BY c.id ASC")
    List<Category> findAllRootCategoriesIncludingInactive();

    @Override
    @NonNull
    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.id ASC")
    List<Category> findAll();

    @Query("SELECT c FROM Category c ORDER BY c.id ASC")
    List<Category> findAllIncludingInactive();
}
