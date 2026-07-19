package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para enviar los datos de un rol con sus permisos")
public class RolesResponseDTO {

    @Schema(description = "ID del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "Cajero, Administrador, Contador")
    private String rol;

    @Schema(description = "Descripción de las funciones del rol", example = "Rol encargado del módulo de facturación y cobros")
    private String descripcion;

    @Schema(description = "Estado del rol (true = Activo, false = Inactivo)", example = "true")
    private Boolean estado;

    @Schema(description = "Listado de permisos detallados que posee este rol")
    private Set<PermisosResponseDTO> permisos;

}
