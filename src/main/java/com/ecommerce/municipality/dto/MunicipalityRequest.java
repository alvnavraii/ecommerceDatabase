package com.ecommerce.municipality.dto;

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
public class MunicipalityRequest {
    public interface Create {}

    @NotNull(message = "El ID de la provincia es obligatorio", groups = Create.class)
    private Long provinceId;

    @NotBlank(message = "El código es obligatorio", groups = Create.class)
    @Size(max = 5, message = "El código no puede tener más de 5 caracteres")
    private String code;

    @NotBlank(message = "El nombre es obligatorio", groups = Create.class)
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;
}
