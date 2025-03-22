package com.ecommerce.municipality;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.ecommerce.municipality.dto.MunicipalityRequest;
import com.ecommerce.municipality.dto.MunicipalityResponse;
import com.ecommerce.municipality.dto.MunicipalityRequest.Create;

import java.util.List;

@RestController
@RequestMapping("/api/v1/municipalities")
@RequiredArgsConstructor
public class MunicipalityController {
    private final MunicipalityService municipalityService;

    @GetMapping
    public ResponseEntity<List<MunicipalityResponse>> getAllMunicipalitys() {
        return ResponseEntity.ok(municipalityService.getAllMunicipalitys());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MunicipalityResponse>> getAllMunicipalitysIncludingInactive() {
        return ResponseEntity.ok(municipalityService.getAllMunicipalitysIncludingInactive());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<MunicipalityResponse> getMunicipalityByCode(@PathVariable String code) {
        return ResponseEntity.ok(municipalityService.getMunicipalityByCode(code));
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MunicipalityResponse>> getInactiveMunicipalitys() {
        return ResponseEntity.ok(municipalityService.getInactiveMunicipalitys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityResponse> getMunicipalityById(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityService.getMunicipalityById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MunicipalityResponse> createMunicipality(
            @Validated(Create.class) @RequestBody MunicipalityRequest request) {
        return ResponseEntity.ok(municipalityService.createMunicipality(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MunicipalityResponse> updateMunicipality(
            @PathVariable Long id,
            @Valid @RequestBody MunicipalityRequest request) {
        return ResponseEntity.ok(municipalityService.updateMunicipality(id, request));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MunicipalityResponse> deactivateMunicipality(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityService.deactivateMunicipality(id));
    }

    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MunicipalityResponse> reactivateMunicipality(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityService.reactivateMunicipality(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MunicipalityResponse> deleteMunicipality(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityService.deleteMunicipality(id));
    }
}
