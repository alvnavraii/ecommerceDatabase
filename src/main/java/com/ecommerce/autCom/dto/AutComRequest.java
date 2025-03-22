package com.ecommerce.autCom.dto;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutComRequest {
    @Size(min = 2, max = 2, message = "El código de la comunidad autónoma debe tener exactamente 2 caracteres")
    private String code;

    @Size(min = 3, max = 3, message = "El código alfa de la comunidad autónoma debe tener exactamente 3 caracteres")
    private String alfaCode;

    @Size(max = 100, message = "El nombre de la comunidad autónoma no puede tener más de 100 caracteres")
    private String name;

    private Long countryId;
}
