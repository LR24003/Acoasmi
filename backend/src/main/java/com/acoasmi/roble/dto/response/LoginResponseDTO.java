package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para enviar los datos de usuario para inicio de sesión")
public class LoginResponseDTO {

    private String token;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String nombreRol;

}
