package com.ecommerce.categoryTranslation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryTranslationRequest {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Language ID is required")
    private Long languageId;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Size(max = 4000, message = "Description must be less than 4000 characters")
    private String description;

    @Size(max = 255, message = "Meta title must be less than 255 characters")
    private String metaTitle;

    @Size(max = 4000, message = "Meta description must be less than 4000 characters")
    private String metaDescription;
}
