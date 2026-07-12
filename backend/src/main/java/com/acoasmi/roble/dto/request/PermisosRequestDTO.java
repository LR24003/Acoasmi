package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO para la creación o actualización de un Permiso")
public class PermisosRequestDTO {

    @NotBlank(message = "El código del permiso no puede estar vacío")
    @Size(max = 50, message = "El código del permiso no puede superar los 50 caracteres")
    @Pattern(regexp = "^[A-Z0-String_]+$", message = "El código del permiso debe estar en mayúsculas y usar guiones bajos (ej. USUARIOS_CREAR)")
    @Schema(description = "Código único e identificable del permiso en mayúsculas", example = "USUARIOS_CREAR")
    private String codigoPermiso;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Explicación clara de qué acción permite realizar este código", example = "Permite registrar nuevos usuarios en el sistema")
    private String descripcion;
}
