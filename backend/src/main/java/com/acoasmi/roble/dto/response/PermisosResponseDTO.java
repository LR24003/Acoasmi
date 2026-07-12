package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para enviar los datos detallados de un permiso")
public class PermisosResponseDTO {

    @Schema(description = "ID del permiso", example = "1")
    private Long id;

    @Schema(description = "Código único del permiso", example = "USUARIOS_CREAR")
    private String codigoPermiso;

    @Schema(description = "Descripción del permiso", example = "Permite registrar nuevos usuarios en el sistema")
    private String descripcion;
}