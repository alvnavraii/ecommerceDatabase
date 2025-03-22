package com.ecommerce.address.dto;

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
public class AddressRequest {
    public interface Create {}

    private Long userId;

    @NotBlank(message = "El dirección es obligatorio", groups = Create.class)
    private String address;

    @NotNull(message = "El Municipalityid es obligatorio", groups = Create.class)
    private Long municipalityId;

    @NotBlank(message = "El Postalcode es obligatorio", groups = Create.class)
    @Size(max = 5, message = "El Postalcode no puede tener más de 5 caracteres")
    private String postalCode;

    @NotNull(message = "El Isdefault es obligatorio", groups = Create.class)
    private Boolean isDefault;
}
