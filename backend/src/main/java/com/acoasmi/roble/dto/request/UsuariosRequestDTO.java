package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "DTO para recibir datos de un usuario (sin ID)")
public class UsuariosRequestDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede superar los 50 caracteres")
    @Schema(description = "Nombre de usuario para inicio de sesión", example = "Cajero01")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Schema(description = "Contraseña del usuario para inicio de sesión", example = "ClaveTemporal01")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    @Schema(description = "Nombres del usuario", example = "Angel Alberto")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    @Schema(description = "Apellidos del usuario", example = "Perez Campos")
    private String apellidos;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    @Schema(description = "Correo electronico del usuario", example = "correoejemplo@acoasmi.com")
    private String email;

    @NotNull(message = "El rol del usuario es obligatorio")
    @Schema(description = "Rol del usuario", example = "1")
    private Long idRol;
}
