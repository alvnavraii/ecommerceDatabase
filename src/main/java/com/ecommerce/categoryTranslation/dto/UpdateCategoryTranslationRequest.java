package com.ecommerce.categoryTranslation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCategoryTranslationRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Size(max = 4000, message = "Description must be at most 4000 characters")
    private String description;

    @Size(max = 255, message = "Meta title must be at most 255 characters")
    private String metaTitle;

    @Size(max = 500, message = "Meta description must be at most 500 characters")
    private String metaDescription;
}
