package com.ecommerce.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    private Long parentId;

    @NotBlank(message = "El slug es requerido")
    private String slug;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    private String description;
}
