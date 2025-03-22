package com.ecommerce.measurement_unit_translation.dto;

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
public class MeasurementUnitTranslationRequest {
    public interface Create {}

    @NotBlank(message = "El nombre es obligatorio", groups = Create.class)
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotNull(message = "El id de la unidad de medida es obligatorio", groups = Create.class)
    private Long measurementUnitId;

    @NotNull(message = "El id del idioma es obligatorio", groups = Create.class)
    private Long languageId;

    @NotNull(message = "El estado es obligatorio", groups = Create.class)
    @Builder.Default
    private Boolean isActive = true;

}
