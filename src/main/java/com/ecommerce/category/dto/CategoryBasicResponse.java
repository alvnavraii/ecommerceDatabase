package com.ecommerce.category.dto;

import com.ecommerce.category.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryBasicResponse {
    private Long id;
    private String slug;
    private Boolean isActive;

    public static CategoryBasicResponse fromEntity(Category entity) {
        if (entity == null) return null;
        return CategoryBasicResponse.builder()
                .id(entity.getId())
                .slug(entity.getSlug())
                .isActive(entity.getIsActive())
                .build();
    }
}
