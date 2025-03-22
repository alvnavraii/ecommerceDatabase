package com.ecommerce.language;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.language.dto.CreateLanguageRequest;
import com.ecommerce.language.dto.LanguageResponse;
import com.ecommerce.language.dto.ReactivateLanguageRequest;
import com.ecommerce.language.dto.UpdateLanguageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LanguageResponse>> getAllLanguagesIncludingInactive() {
        return ResponseEntity.ok(languageService.getAllLanguagesIncludingInactive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> getLanguageById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.getLanguageById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<LanguageResponse> getLanguageByCode(@PathVariable String code) {
        return ResponseEntity.ok(languageService.getLanguageByCode(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> createLanguage(@Valid @RequestBody CreateLanguageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(languageService.createLanguage(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLanguageRequest request) {
        return ResponseEntity.ok(languageService.updateLanguage(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> deleteLanguage(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.deleteLanguage(id));
    }

    @PostMapping("/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> reactivateLanguage(@Valid @RequestBody ReactivateLanguageRequest request) {
        return ResponseEntity.ok(languageService.reactivateLanguage(request.getId()));
    }
}
