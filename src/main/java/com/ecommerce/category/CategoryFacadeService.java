package com.ecommerce.category;

import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.categoryTranslation.CategoryTranslationService;
import com.ecommerce.categoryTranslation.dto.CategoryTranslationResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryFacadeService {
    private final CategoryService categoryService;
    private final CategoryTranslationService categoryTranslationService;

    public CategoryResponse getCategoryWithTranslations(Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return addTranslationsRecursively(category);
    }

    public List<CategoryResponse> getRootCategoriesWithTranslations() {
        return categoryService.getRootCategories().stream()
                .map(this::addTranslationsRecursively)
                .toList();
    }

    private CategoryResponse addTranslationsRecursively(CategoryResponse category) {
        if (category == null) {
            return null;
        }

        List<CategoryTranslationResponse> translations = categoryTranslationService.getCategoryTranslations(category.getId());
        List<CategoryResponse> children = category.getChildren().stream()
                .map(this::addTranslationsRecursively)
                .toList();

        return category
                .withTranslations(translations)
                .withChildren(children);
    }
}
