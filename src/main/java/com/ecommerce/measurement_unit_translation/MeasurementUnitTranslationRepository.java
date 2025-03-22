package com.ecommerce.measurement_unit_translation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementUnitTranslationRepository extends JpaRepository<MeasurementUnitTranslation, Long> {
    @Override
    @NonNull
    @Query("SELECT e FROM MeasurementUnitTranslation e WHERE e.isActive = true ORDER BY e.id ASC")
    List<MeasurementUnitTranslation> findAll();

    @Query("SELECT e FROM MeasurementUnitTranslation e ORDER BY e.id ASC")
    List<MeasurementUnitTranslation> findAllIncludingInactive();

    @Query("SELECT e FROM MeasurementUnitTranslation e WHERE e.isActive = false ORDER BY e.id ASC")
    List<MeasurementUnitTranslation> findAllInactive();
}
