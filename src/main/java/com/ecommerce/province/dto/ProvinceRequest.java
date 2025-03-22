package com.ecommerce.province.dto;

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
public class ProvinceRequest {
    public interface Create {}
    
    @NotBlank(message = "El código de provincia es obligatorio", groups = Create.class)
    @Size(min = 2, max = 2, message = "El código de provincia debe tener exactamente 2 caracteres")
    private String code;

    @NotBlank(message = "El código alfa de provincia es obligatorio", groups = Create.class)
    @Size(min = 3, max = 3, message = "El código alfa de provincia debe tener exactamente 3 caracteres")
    private String alfaCode;

    @NotBlank(message = "El nombre de provincia es obligatorio", groups = Create.class)
    @Size(min = 3, max = 100, message = "El nombre de provincia debe tener entre 3 y 100 caracteres")
    private String name;

    @NotNull(message = "El ID de la autonomía es obligatorio", groups = Create.class)
    private Long autComId;
}
