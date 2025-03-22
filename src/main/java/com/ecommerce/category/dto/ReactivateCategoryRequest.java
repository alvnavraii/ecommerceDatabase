package com.ecommerce.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactivateCategoryRequest {
    @NotNull(message = "El ID de la categoría es requerido")
    private Long id;
}
