package com.ecommerce.language;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("SELECT l FROM Language l ORDER BY l.id ASC")
    List<Language> findAllByOrderByIdAsc();

    @Query("SELECT l FROM Language l WHERE l.code = :code AND l.isActive = true ORDER BY l.id ASC")
    Optional<Language> findByCodeAndIsActiveTrue(@Param("code") String code);

    @Query("SELECT COUNT(l) > 0 FROM Language l WHERE l.code = :code")
    boolean existsByCode(@Param("code") String code);

    @Override
    @Query("SELECT l FROM Language l WHERE l.isActive = true ORDER BY l.id ASC")
    @NonNull
    List<Language> findAll();

    @Query("SELECT l FROM Language l ORDER BY l.id ASC")
    List<Language> findAllIncludingInactive();
}
