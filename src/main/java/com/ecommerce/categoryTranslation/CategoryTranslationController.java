package com.ecommerce.categoryTranslation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.categoryTranslation.dto.CategoryTranslationResponse;
import com.ecommerce.categoryTranslation.dto.CreateCategoryTranslationRequest;
import com.ecommerce.categoryTranslation.dto.UpdateCategoryTranslationRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories/translations")
@RequiredArgsConstructor
public class CategoryTranslationController {
    private final CategoryTranslationService categoryTranslationService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CategoryTranslationResponse>> getCategoryTranslations(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryTranslationService.getCategoryTranslations(categoryId));
    }

    @GetMapping("/category/{categoryId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryTranslationResponse>> getAllCategoryTranslations(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryTranslationService.getAllCategoryTranslations(categoryId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryTranslationResponse> createCategoryTranslation(
            @RequestBody @Valid CreateCategoryTranslationRequest request
    ) {
        return ResponseEntity.ok(categoryTranslationService.createCategoryTranslation(request));
    }

    @PutMapping("/category/{categoryId}/language/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryTranslationResponse> updateCategoryTranslation(
            @PathVariable Long categoryId,
            @PathVariable Long languageId,
            @RequestBody @Valid UpdateCategoryTranslationRequest request
    ) {
        return ResponseEntity.ok(categoryTranslationService.updateCategoryTranslation(categoryId, languageId, request));
    }

    @DeleteMapping("/category/{categoryId}/language/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryTranslationResponse> deleteCategoryTranslation(
            @PathVariable Long categoryId,
            @PathVariable Long languageId
    ) {
        return ResponseEntity.ok(categoryTranslationService.deleteCategoryTranslation(categoryId, languageId));
    }

    @PostMapping("/category/{categoryId}/language/{languageId}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryTranslationResponse> reactivateCategoryTranslation(
            @PathVariable Long categoryId,
            @PathVariable Long languageId
    ) {
        return ResponseEntity.ok(categoryTranslationService.reactivateCategoryTranslation(categoryId, languageId));
    }
}
