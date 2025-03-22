package com.ecommerce.language.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactivateLanguageRequest {
    @NotNull(message = "El ID del idioma es requerido")
    private Long id;
}
