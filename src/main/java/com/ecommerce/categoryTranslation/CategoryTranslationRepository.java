package com.ecommerce.categoryTranslation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;

@Repository
public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, Long> {
    @Query(value = "SELECT COUNT(*) FROM CATEGORY_TRANSLATIONS WHERE CATEGORY_ID = :categoryId AND LANGUAGE_ID = :languageId AND IS_ACTIVE = 1", nativeQuery = true)
    int countByCategoryIdAndLanguageId(Long categoryId, Long languageId);

    default boolean existsByCategoryIdAndLanguageId(Long categoryId, Long languageId) {
        return countByCategoryIdAndLanguageId(categoryId, languageId) > 0;
    }

    @NonNull
    @Override
    @Query(value = "SELECT * FROM CATEGORY_TRANSLATIONS WHERE IS_ACTIVE = 1 ORDER BY CATEGORY_ID ASC, LANGUAGE_ID ASC", nativeQuery = true)
    List<CategoryTranslation> findAll();

    @Query(value = "SELECT * FROM CATEGORY_TRANSLATIONS ORDER BY CATEGORY_ID ASC, LANGUAGE_ID ASC", nativeQuery = true)
    List<CategoryTranslation> findAllIncludingInactive();

    @Query(value = "SELECT * FROM CATEGORY_TRANSLATIONS WHERE CATEGORY_ID = :categoryId AND IS_ACTIVE = 1 ORDER BY LANGUAGE_ID ASC", nativeQuery = true)
    List<CategoryTranslation> findByCategoryId(Long categoryId);

    @Query(value = "SELECT * FROM CATEGORY_TRANSLATIONS WHERE LANGUAGE_ID = :languageId AND IS_ACTIVE = 1 ORDER BY CATEGORY_ID ASC", nativeQuery = true)
    List<CategoryTranslation> findByLanguageId(Long languageId);

    @Query(value = "SELECT * FROM CATEGORY_TRANSLATIONS WHERE CATEGORY_ID = :categoryId AND LANGUAGE_ID = :languageId AND IS_ACTIVE = 1", nativeQuery = true)
    CategoryTranslation findByCategoryIdAndLanguageId(Long categoryId, Long languageId);
}
