package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir los datos de un municipio")
public class MunicipiosRequestDTO {

    @NotBlank(message = "El código del municipio es obligatorio")
    @Size(max = 10, message = "El código del municipio no puede exceder los 10 caracteres")
    @Schema(description = "Código geopolítico o de Hacienda del municipio", example = "0601")
    private String codigoMunicipio;

    @NotBlank(message = "El nombre del municipio es obligatorio")
    @Size(max = 100, message = "El nombre del municipio no puede exceder los 100 caracteres")
    @Schema(description = "Nombre oficial del municipio", example = "San Vicente Sur")
    private String nombre;

    @NotBlank(message = "El nombre del departamento al que pertenece el municipio es obligatorio")
    @Schema(description = "Nombre del departamento al que pertenece el municipio", example = "San Vicente")
    private String nombreDepartamento;
}