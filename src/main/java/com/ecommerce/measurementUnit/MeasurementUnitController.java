package com.ecommerce.measurementUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.measurementUnit.dto.MeasurementUnitRequest;
import com.ecommerce.measurementUnit.dto.MeasurementUnitResponse;
import com.ecommerce.measurementUnit.dto.MeasurementUnitRequest.Create;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/measurementunits")
@RequiredArgsConstructor
public class MeasurementUnitController {
    private final MeasurementUnitService measurementunitService;

    @GetMapping
    public ResponseEntity<List<MeasurementUnitResponse>> getAllMeasurementUnits() {
        return ResponseEntity.ok(measurementunitService.getAllMeasurementUnits());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeasurementUnitResponse>> getAllMeasurementUnitsIncludingInactive() {
        return ResponseEntity.ok(measurementunitService.getAllMeasurementUnitsIncludingInactive());
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeasurementUnitResponse>> getInactiveMeasurementUnits() {
        return ResponseEntity.ok(measurementunitService.getInactiveMeasurementUnits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> getMeasurementUnitById(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunitService.getMeasurementUnitById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitResponse> createMeasurementUnit(
            @Validated(Create.class) @RequestBody MeasurementUnitRequest request) {
        return ResponseEntity.ok(measurementunitService.createMeasurementUnit(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitResponse> updateMeasurementUnit(
            @PathVariable Long id,
            @Valid @RequestBody MeasurementUnitRequest request) {
        return ResponseEntity.ok(measurementunitService.updateMeasurementUnit(id, request));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitResponse> deactivateMeasurementUnit(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunitService.deactivateMeasurementUnit(id));
    }

    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitResponse> reactivateMeasurementUnit(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunitService.reactivateMeasurementUnit(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitResponse> deleteMeasurementUnit(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunitService.deleteMeasurementUnit(id));
    }
}
