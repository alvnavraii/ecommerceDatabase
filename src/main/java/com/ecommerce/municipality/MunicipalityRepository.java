package com.ecommerce.municipality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    @Override
    @NonNull
    @Query("SELECT e FROM Municipality e WHERE e.isActive = true ORDER BY e.id ASC")
    List<Municipality> findAll();

    @Query("SELECT e FROM Municipality e ORDER BY e.id ASC")
    List<Municipality> findAllIncludingInactive();

    @Query("SELECT e FROM Municipality e WHERE e.isActive = false ORDER BY e.id ASC")
    List<Municipality> findAllInactive();

    @Query("SELECT e FROM Municipality e WHERE e.code = :code")
    Optional<Municipality> findByCode(@Param("code") String code);
}
