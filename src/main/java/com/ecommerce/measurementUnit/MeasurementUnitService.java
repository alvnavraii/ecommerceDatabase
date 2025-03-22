package com.ecommerce.measurementUnit;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.common.exception.OracleException;
import com.ecommerce.measurementUnit.dto.MeasurementUnitRequest;
import com.ecommerce.measurementUnit.dto.MeasurementUnitResponse;
import com.ecommerce.common.Audit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeasurementUnitService {
    private final MeasurementUnitRepository measurementunitRepository;

    public List<MeasurementUnitResponse> getAllMeasurementUnits() {
        return measurementunitRepository.findAll().stream()
                .map(MeasurementUnitResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeasurementUnitResponse> getAllMeasurementUnitsIncludingInactive() {
        return measurementunitRepository.findAllIncludingInactive().stream()
                .map(MeasurementUnitResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeasurementUnitResponse> getInactiveMeasurementUnits() {
        return measurementunitRepository.findAllInactive().stream()
                .map(MeasurementUnitResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public MeasurementUnitResponse getMeasurementUnitById(Long id) {
        return MeasurementUnitResponse.fromEntity(
            measurementunitRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"))
        );
    }

    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request) {
        var audit = Audit.builder()
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedBy("system")
                .updatedAt(LocalDateTime.now())
                .build();

        var measurementunit = MeasurementUnit.builder()
                .name(request.getName())
                .code(request.getCode())
                .symbol(request.getSymbol())
                .isActive(true)
                .audit(audit)
                .build();

        return MeasurementUnitResponse.fromEntity(measurementunitRepository.save(measurementunit));
    }

    public MeasurementUnitResponse updateMeasurementUnit(Long id, MeasurementUnitRequest request) {
        var measurementunit = measurementunitRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        if (request.getName() != null && !request.getName().isBlank()) {
            measurementunit.setName(request.getName());
        }

        if (request.getCode() != null && !request.getCode().isBlank()) {
            measurementunit.setCode(request.getCode());
        }

        if (request.getSymbol() != null && !request.getSymbol().isBlank()) {
            measurementunit.setSymbol(request.getSymbol());
        }

        if (request.getIsActive() != null) {
            measurementunit.setIsActive(request.getIsActive());
        }

        measurementunit.getAudit().setUpdatedBy("system");
        measurementunit.getAudit().setUpdatedAt(LocalDateTime.now());

        return MeasurementUnitResponse.fromEntity(measurementunitRepository.save(measurementunit));
    }

    public MeasurementUnitResponse deactivateMeasurementUnit(Long id) {
        var measurementunit = measurementunitRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        measurementunit.setIsActive(false);
        measurementunit.getAudit().setUpdatedBy("system");
        measurementunit.getAudit().setUpdatedAt(LocalDateTime.now());

        return MeasurementUnitResponse.fromEntity(measurementunitRepository.save(measurementunit));
    }

    public MeasurementUnitResponse reactivateMeasurementUnit(Long id) {
        var measurementunit = measurementunitRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        measurementunit.setIsActive(true);
        measurementunit.getAudit().setUpdatedBy("system");
        measurementunit.getAudit().setUpdatedAt(LocalDateTime.now());

        return MeasurementUnitResponse.fromEntity(measurementunitRepository.save(measurementunit));
    }

    public MeasurementUnitResponse deleteMeasurementUnit(Long id) {
        return deactivateMeasurementUnit(id);
    }
}
