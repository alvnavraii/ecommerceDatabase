package com.ecommerce.province;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query("SELECT p FROM Province p ORDER BY p.id ASC")
    List<Province> findAllByOrderByIdAsc();

    @Query("SELECT p FROM Province p WHERE p.code = :code AND p.isActive = true ORDER BY p.id ASC")
    Optional<Province> findByCodeAndIsActiveTrue(@Param("code") String code);

    @Query("SELECT p FROM Province p WHERE p.alfaCode = :alfaCode AND p.isActive = true ORDER BY p.id ASC")
    Optional<Province> findByAlfaCodeAndIsActiveTrue(@Param("alfaCode") String alfaCode);

    @Query("SELECT COUNT(p) > 0 FROM Province p WHERE p.code = :code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT COUNT(p) > 0 FROM Province p WHERE p.alfaCode = :alfaCode")
    boolean existsByAlfaCode(@Param("alfaCode") String alfaCode);

    Optional<Province> findByNameIgnoreCase(String name);
}
