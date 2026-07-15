package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para enviar los datos de un usuario")
public class UsuariosResponseDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario para inicio de sesión", example = "Cajero01")
    private String usuario;

    @Schema(description = "Nombres del usuario", example = "Angel Alberto")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Perez Campos")
    private String apellidos;

    @Schema(description = "Correo electronico del usuario", example = "correoejemplo@acoasmi.com")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Hora y fecha de la creacion del usuario", example = "20/05/2026 12:30")
    private LocalDateTime fechaCreacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Hora y fecha del ultimo acceso al sistema", example = "20/05/2026 12:30")
    private LocalDateTime ultimoAcceso;

    @Schema(description = "Rol del usuario", example = "Cajero")
    private String nombreRol;

    @Schema(description = "Estado del usuario (true = Activo, false = Inactivo)", example = "true")
    private Boolean estado;
}
