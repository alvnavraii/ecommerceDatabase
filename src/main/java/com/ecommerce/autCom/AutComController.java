package com.ecommerce.autCom;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.autCom.dto.AutComRequest;
import com.ecommerce.autCom.dto.AutComResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/autComs")
@RequiredArgsConstructor
public class AutComController {
    private final AutComService autComService;

    @GetMapping("/")
    public ResponseEntity<List<AutComResponse>> getAllAutComs() {
        return ResponseEntity.ok(autComService.getAllAutComs());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AutComResponse>> getAllAutComsIncludingInactive() {
        return ResponseEntity.ok(autComService.getAllAutComsIncludingInactive());
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AutComResponse>> getInactiveAutComs() {
        return ResponseEntity.ok(autComService.getInactiveAutComs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutComResponse> getAutComById(@PathVariable Long id) {
        return ResponseEntity.ok(autComService.getAutComById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<AutComResponse> getAutComByCode(@PathVariable String code) {
        return ResponseEntity.ok(autComService.getAutComByCode(code));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutComResponse> createAutCom(@Valid @RequestBody AutComRequest request) {
        return ResponseEntity.ok(autComService.createAutCom(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutComResponse> updateAutCom(@PathVariable Long id, @Valid @RequestBody AutComRequest request) {
        return ResponseEntity.ok(autComService.updateAutCom(id, request));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutComResponse> deactivateAutCom(@PathVariable Long id) {
        return ResponseEntity.ok(autComService.deactivateAutCom(id));
    }

    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutComResponse> reactivateAutCom(@PathVariable Long id) {
        return ResponseEntity.ok(autComService.reactivateAutCom(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutComResponse> deleteAutCom(@PathVariable Long id) {
        return ResponseEntity.ok(autComService.deleteAutCom(id));
    }
}