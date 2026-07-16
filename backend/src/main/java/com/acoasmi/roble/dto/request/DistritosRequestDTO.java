package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación o actualización de un distrito")
public class DistritosRequestDTO {

    @NotBlank(message = "El código del distrito es obligatorio")
    @Schema(description = "Código del distrito según MH", example = "11")
    private String codigoDistrito;

    @NotBlank(message = "El nombre del distrito es obligatorio")
    @Schema(description = "Nombre oficial del distrito", example = "Tecoluca")
    private String nombre;

    @NotBlank(message = "El nombre del departamento es obligatorio")
    @Schema(description = "Nombre del departamento al que pertenece el distrito", example = "San Vicente")
    private String nombreDepartamento;

    @NotBlank(message = "El nombre del municipio es obligatorio")
    @Schema(description = "Nombre del municipio al que pertenece el distrito", example = "San Vicente Sur")
    private String nombreMunicipio;
}
