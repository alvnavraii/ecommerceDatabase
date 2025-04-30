package com.ecommerce.category.dto;

import com.ecommerce.category.Category;
import com.ecommerce.categoryTranslation.dto.CategoryTranslationResponse;
import com.ecommerce.common.dto.AuditResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class CategoryResponse {
    private Long id;
    private String slug;
    private String name;
    private String description;
    private Boolean isActive;
    private List<CategoryTranslationResponse> translations;
    private ParentCategoryInfo parent;
    private List<CategoryResponse> children;
    private AuditResponse audit;

    @Data
    @Builder
    public static class ParentCategoryInfo {
        private Long id;
        private String slug;
    }

    public static CategoryResponse fromEntity(Category category) {
        if (category == null) {
            return null;
        }

        List<CategoryResponse> children = category.getChildren() != null
                ? category.getChildren().stream()
                        .filter(Category::getIsActive)
                        .map(CategoryResponse::fromEntity)
                        .collect(Collectors.toList())
                : List.of();

        ParentCategoryInfo parent = null;
        if (category.getParentId() != null && category.getParent() != null) {
            parent = ParentCategoryInfo.builder()
                    .id(category.getParent().getId())
                    .slug(category.getParent().getSlug())
                    .build();
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .slug(category.getSlug())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .translations(List.of())
                .parent(parent)
                .children(children)
                .audit(AuditResponse.fromEntity(category.getAudit()))
                .build();
    }

    public static CategoryResponse fromEntityIncludingInactive(Category category) {
        if (category == null) {
            return null;
        }

        List<CategoryResponse> children = category.getChildren() != null
                ? category.getChildren().stream()
                        .map(CategoryResponse::fromEntityIncludingInactive)
                        .collect(Collectors.toList())
                : List.of();

        ParentCategoryInfo parent = null;
        if (category.getParentId() != null && category.getParent() != null) {
            parent = ParentCategoryInfo.builder()
                    .id(category.getParent().getId())
                    .slug(category.getParent().getSlug())
                    .build();
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .slug(category.getSlug())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .translations(List.of())
                .parent(parent)
                .children(children)
                .audit(AuditResponse.fromEntity(category.getAudit()))
                .build();
    }
}
