package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir los datos de un departamento")
public class DepartamentosRequestDTO {

    @NotNull(message = "El codigo del departamento es obligatorio")
    @Schema(description = "Codigo del departamento según MH", example = "10")
    private Integer codigoDepartamento;

    @NotBlank(message = "El nombre del departamento es obligatorio")
    @Schema(description = "Nombre del departamento", example = "San Vicente")
    private String nombreDepartamento;
}
