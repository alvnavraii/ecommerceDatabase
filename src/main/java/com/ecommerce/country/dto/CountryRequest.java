package com.ecommerce.country.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryRequest {
    @NotBlank(message = "El código del país es obligatorio")
    @Size(min = 2, max = 2, message = "El código del país debe tener exactamente 2 caracteres")
    private String code;

    @Size(max = 100, message = "El nombre del país no puede tener más de 100 caracteres")
    private String name;
}
