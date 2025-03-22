package com.ecommerce.measurement_unit_translation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.ecommerce.measurement_unit_translation.dto.MeasurementUnitTranslationRequest;
import com.ecommerce.measurement_unit_translation.dto.MeasurementUnitTranslationResponse;
import com.ecommerce.measurement_unit_translation.dto.MeasurementUnitTranslationRequest.Create;

import java.util.List;

@RestController
@RequestMapping("/api/v1/measurement-unit-translations")
@RequiredArgsConstructor
public class MeasurementUnitTranslationController {
    private final MeasurementUnitTranslationService measurementunittranslationService;

    @GetMapping
    public ResponseEntity<List<MeasurementUnitTranslationResponse>> getAllMeasurementUnitTranslations() {
        return ResponseEntity.ok(measurementunittranslationService.getAllMeasurementUnitTranslations());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeasurementUnitTranslationResponse>> getAllMeasurementUnitTranslationsIncludingInactive() {
        return ResponseEntity.ok(measurementunittranslationService.getAllMeasurementUnitTranslationsIncludingInactive());
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeasurementUnitTranslationResponse>> getInactiveMeasurementUnitTranslations() {
        return ResponseEntity.ok(measurementunittranslationService.getInactiveMeasurementUnitTranslations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementUnitTranslationResponse> getMeasurementUnitTranslationById(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunittranslationService.getMeasurementUnitTranslationById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitTranslationResponse> createMeasurementUnitTranslation(
            @Validated(Create.class) @RequestBody MeasurementUnitTranslationRequest request) {
        return ResponseEntity.ok(measurementunittranslationService.createMeasurementUnitTranslation(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitTranslationResponse> updateMeasurementUnitTranslation(
            @PathVariable Long id,
            @Valid @RequestBody MeasurementUnitTranslationRequest request) {
        return ResponseEntity.ok(measurementunittranslationService.updateMeasurementUnitTranslation(id, request));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitTranslationResponse> deactivateMeasurementUnitTranslation(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunittranslationService.deactivateMeasurementUnitTranslation(id));
    }

    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitTranslationResponse> reactivateMeasurementUnitTranslation(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunittranslationService.reactivateMeasurementUnitTranslation(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeasurementUnitTranslationResponse> deleteMeasurementUnitTranslation(@PathVariable Long id) {
        return ResponseEntity.ok(measurementunittranslationService.deleteMeasurementUnitTranslation(id));
    }
}
