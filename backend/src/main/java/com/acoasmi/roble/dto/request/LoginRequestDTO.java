package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir los datos de usuario para inicio de sesión")
public class LoginRequestDTO {

    @Schema(description = "Usuario asignado para inicio de sesión", example = "Administrador01")
    private String usuario;

    @Schema(description = "Contraseña creada por el usuario para inicio de sesión", example = "ClaveTemporal26")
    private String password;

}
