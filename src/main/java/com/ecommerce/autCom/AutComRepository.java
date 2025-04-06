package com.ecommerce.autCom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutComRepository extends JpaRepository<AutCom, Long> {
    @Query("SELECT a FROM AutCom a ORDER BY a.id ASC")
    List<AutCom> findAllByOrderByIdAsc();

    @Query("SELECT a FROM AutCom a WHERE a.code = :code AND a.isActive = true ORDER BY a.id ASC")
    Optional<AutCom> findByCodeAndIsActiveTrue(@Param("code") String code);

    @Query("SELECT COUNT(a) > 0 FROM AutCom a WHERE a.code = :code")
    boolean existsByCode(@Param("code") String code);
}
