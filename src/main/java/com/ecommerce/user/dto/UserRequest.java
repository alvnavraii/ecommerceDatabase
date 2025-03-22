package com.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    public interface Default {}
    public interface Create extends Default {}

    @NotBlank(message = "El email es obligatorio", groups = Create.class)
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = Create.class)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String password;

    @NotBlank(message = "El nombre es obligatorio", groups = Create.class)
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio", groups = Create.class)
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String lastName;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String phone;

    @Size(max = 255, message = "La URL del avatar no puede tener más de 255 caracteres")
    private String avatarUrl;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private boolean isAdmin = false;
}