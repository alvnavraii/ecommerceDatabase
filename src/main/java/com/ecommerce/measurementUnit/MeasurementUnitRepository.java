package com.ecommerce.measurementUnit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Long> {
    @Override
    @NonNull
    @Query("SELECT e FROM MeasurementUnit e WHERE e.isActive = true ORDER BY e.id ASC")
    List<MeasurementUnit> findAll();

    @Query("SELECT e FROM MeasurementUnit e ORDER BY e.id ASC")
    List<MeasurementUnit> findAllIncludingInactive();

    @Query("SELECT e FROM MeasurementUnit e WHERE e.isActive = false ORDER BY e.id ASC")
    List<MeasurementUnit> findAllInactive();
}
