package com.ecommerce.categoryTranslation.dto;

import com.ecommerce.category.Category;
import com.ecommerce.categoryTranslation.CategoryTranslation;
import com.ecommerce.common.dto.AuditResponse;
import com.ecommerce.language.Language;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryTranslationResponse {
    private String name;
    private String description;
    private String metaTitle;
    private String metaDescription;
    private Boolean isActive;
    private CategoryInfo category;
    private LanguageInfo language;
    private AuditResponse audit;

    @Data
    @Builder
    public static class CategoryInfo {
        private Long id;
        private String slug;
        private Boolean isActive;
    }

    @Data
    @Builder
    public static class LanguageInfo {
        private Long id;
        private String code;
        private Boolean isActive;
    }

    public static CategoryTranslationResponse fromEntity(CategoryTranslation translation) {
        if (translation == null) {
            return null;
        }

        Category category = translation.getCategory();
        CategoryInfo categoryInfo = CategoryInfo.builder()
                .id(category.getId())
                .slug(category.getSlug())
                .isActive(category.getIsActive())
                .build();

        Language language = translation.getLanguage();
        LanguageInfo languageInfo = LanguageInfo.builder()
                .id(language.getId())
                .code(language.getCode())
                .isActive(language.getIsActive())
                .build();

        return CategoryTranslationResponse.builder()
                .name(translation.getName())
                .description(translation.getDescription())
                .metaTitle(translation.getMetaTitle())
                .metaDescription(translation.getMetaDescription())
                .isActive(translation.getIsActive())
                .category(categoryInfo)
                .language(languageInfo)
                .audit(AuditResponse.fromEntity(translation.getAudit()))
                .build();
    }
}
