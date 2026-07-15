package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir los datos de creación o actualización de un Rol")
public class RolesRequestDTO {

    @NotBlank(message = "El nombre del rol no puede estar vacío")
    @Size(max = 50, message = "El nombre del rol no puede superar los 50 caracteres")
    @Schema(description = "Nombre único del rol en el sistema", example = "Cajero")
    private String nombreRol;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción detallada de las funciones del rol", example = "Rol encargado del módulo de facturación y cobros")
    private String descripcion;

    @NotEmpty(message = "El rol debe tener al menos un permiso asignado")
    @Schema(description = "Listado de IDs de los permisos asignados a este rol", example = "[1, 2, 5]")
    private Set<Long> permisosIds;

}
