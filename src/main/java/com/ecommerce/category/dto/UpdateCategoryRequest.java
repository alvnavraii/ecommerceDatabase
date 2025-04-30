package com.ecommerce.category.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCategoryRequest {
    private Long parentId;

    @Size(max = 100, message = "Slug must be at most 100 characters")
    private String slug;

    private String name;

    private String description;
}
