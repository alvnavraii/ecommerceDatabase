package com.ecommerce.categoryTranslation;

import com.ecommerce.category.Category;
import com.ecommerce.category.CategoryService;
import com.ecommerce.categoryTranslation.dto.CategoryTranslationResponse;
import com.ecommerce.categoryTranslation.dto.CreateCategoryTranslationRequest;
import com.ecommerce.categoryTranslation.dto.UpdateCategoryTranslationRequest;
import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.language.Language;
import com.ecommerce.language.LanguageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryTranslationService {
    private final CategoryTranslationRepository categoryTranslationRepository;
    private final CategoryService categoryService;
    private final LanguageService languageService;

    public List<CategoryTranslationResponse> findAll() {
        return categoryTranslationRepository.findAll()
                .stream()
                .filter(CategoryTranslation::getIsActive)
                .map(CategoryTranslationResponse::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryTranslationResponse> findAllIncludingInactive() {
        return categoryTranslationRepository.findAllIncludingInactive()
                .stream()
                .map(CategoryTranslationResponse::fromEntity)
                .toList();
    }

    public List<CategoryTranslationResponse> findByCategoryId(Long categoryId) {
        return categoryTranslationRepository.findByCategoryId(categoryId)
                .stream()
                .filter(CategoryTranslation::getIsActive)
                .map(CategoryTranslationResponse::fromEntity)
                .toList();
    }

    public List<CategoryTranslationResponse> findByLanguageId(Long languageId) {
        return categoryTranslationRepository.findByLanguageId(languageId)
                .stream()
                .filter(CategoryTranslation::getIsActive)
                .map(CategoryTranslationResponse::fromEntity)
                .toList();
    }

    public CategoryTranslationResponse findByCategoryIdAndLanguageId(Long categoryId, Long languageId) {
        CategoryTranslation translation = categoryTranslationRepository.findByCategoryIdAndLanguageId(categoryId, languageId);
        if (translation == null || !translation.getIsActive()) {
            throw new OracleException("ORA-0002", "Category translation not found");
        }
        return CategoryTranslationResponse.fromEntity(translation);
    }

    public List<CategoryTranslationResponse> getCategoryTranslations(Long categoryId) {
        return categoryTranslationRepository.findByCategoryId(categoryId).stream()
                .filter(CategoryTranslation::getIsActive)
                .map(CategoryTranslationResponse::fromEntity)
                .toList();
    }

    public List<CategoryTranslationResponse> getAllCategoryTranslations(Long categoryId) {
        return categoryTranslationRepository.findByCategoryId(categoryId).stream()
                .map(CategoryTranslationResponse::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryTranslationResponse createCategoryTranslation(CreateCategoryTranslationRequest request) {
        if (categoryTranslationRepository.existsByCategoryIdAndLanguageId(request.getCategoryId(), request.getLanguageId())) {
            throw new OracleException("ORA-0001", "Unique constraint (CATEGORY_TRANSLATIONS.UK_CATEGORY_TRANSLATION) violated");
        }

        Category category = categoryService.findEntityById(request.getCategoryId());
        Language language = languageService.findEntityById(request.getLanguageId());

        if (!category.getIsActive()) {
            throw new OracleException("ORA-0003", "Cannot create translation: category is inactive");
        }

        if (!language.getIsActive()) {
            throw new OracleException("ORA-0003", "Cannot create translation: language is inactive");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        CategoryTranslation translation = CategoryTranslation.builder()
                .category(category)
                .language(language)
                .name(request.getName())
                .description(request.getDescription())
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .isActive(true)
                .audit(Audit.builder()
                        .createdAt(now)
                        .updatedAt(now)
                        .createdBy(username)
                        .updatedBy(username)
                        .build())
                .build();

        return CategoryTranslationResponse.fromEntity(categoryTranslationRepository.save(translation));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryTranslationResponse updateCategoryTranslation(Long categoryId, Long languageId, UpdateCategoryTranslationRequest request) {
        CategoryTranslation translation = categoryTranslationRepository.findByCategoryIdAndLanguageId(categoryId, languageId);
        if (translation == null || !translation.getIsActive()) {
            throw new OracleException("ORA-0002", "Category translation not found");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        translation.setName(request.getName());
        translation.setDescription(request.getDescription());
        translation.setMetaTitle(request.getMetaTitle());
        translation.setMetaDescription(request.getMetaDescription());
        translation.getAudit().setUpdatedAt(now);
        translation.getAudit().setUpdatedBy(username);

        return CategoryTranslationResponse.fromEntity(categoryTranslationRepository.save(translation));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryTranslationResponse deleteCategoryTranslation(Long categoryId, Long languageId) {
        CategoryTranslation translation = categoryTranslationRepository.findByCategoryIdAndLanguageId(categoryId, languageId);
        if (translation == null) {
            throw new OracleException("ORA-0002", "Category translation not found");
        }

        translation.setIsActive(false);
        translation.getAudit().setUpdatedAt(LocalDateTime.now());
        translation.getAudit().setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return CategoryTranslationResponse.fromEntity(translation);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryTranslationResponse reactivateCategoryTranslation(Long categoryId, Long languageId) {
        CategoryTranslation translation = categoryTranslationRepository.findByCategoryIdAndLanguageId(categoryId, languageId);
        if (translation == null) {
            throw new OracleException("ORA-0002", "Category translation not found");
        }

        if (translation.getIsActive()) {
            throw new OracleException("ORA-0003", "Category translation is already active");
        }

        if (!translation.getCategory().getIsActive()) {
            throw new OracleException("ORA-0003", "Cannot reactivate translation: category is inactive");
        }

        if (!translation.getLanguage().getIsActive()) {
            throw new OracleException("ORA-0003", "Cannot reactivate translation: language is inactive");
        }

        translation.setIsActive(true);
        translation.getAudit().setUpdatedAt(LocalDateTime.now());
        translation.getAudit().setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return CategoryTranslationResponse.fromEntity(categoryTranslationRepository.save(translation));
    }
}
