package com.ecommerce.province;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.ecommerce.province.dto.ProvinceResponse;
import com.ecommerce.province.dto.ProvinceRequest;
import com.ecommerce.province.dto.ProvinceRequest.Create;

@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<List<ProvinceResponse>> getAllProvinces() {
        return ResponseEntity.ok(provinceService.getAllProvinces());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProvinceResponse>> getAllProvincesIncludingInactive() {
        return ResponseEntity.ok(provinceService.getAllProvincesIncludingInactive());
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProvinceResponse>> getInactiveProvinces() {
        return ResponseEntity.ok(provinceService.getInactiveProvinces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinceResponse> getProvinceById(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.getProvinceById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProvinceResponse> createProvince(@Validated(Create.class) @RequestBody ProvinceRequest request) {
        return ResponseEntity.ok(provinceService.createProvince(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProvinceResponse> updateProvince(@PathVariable Long id, @Valid @RequestBody ProvinceRequest request) {
        return ResponseEntity.ok(provinceService.updateProvince(id, request));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProvinceResponse> deactivateProvince(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.deactivateProvince(id));
    }

    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProvinceResponse> reactivateProvince(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.reactivateProvince(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProvinceResponse> deleteProvince(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.deleteProvince(id));
    }
}