package com.ecommerce.country;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT c FROM Country c ORDER BY c.id ASC")
    List<Country> findAllByOrderByIdAsc();

    @Query("SELECT c FROM Country c WHERE c.code = :code AND c.isActive = true ORDER BY c.id ASC")
    Optional<Country> findByCodeAndIsActiveTrue(@Param("code") String code);

    @Query("SELECT COUNT(c) > 0 FROM Country c WHERE c.code = :code")
    boolean existsByCode(@Param("code") String code);
}
