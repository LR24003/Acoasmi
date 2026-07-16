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
@Schema(description = "DTO para enviar los datos de un departamento")
public class DepartamentosResponseDTO {

    @Schema(description = "ID del departamento en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Codigo del departamento según MH", example = "10")
    private String codigoDepartamento;

    @Schema(description = "Nombre del departamento", example = "San Vicente")
    private String nombre;

    @Schema(description = "Estado del departamento en la base de datos (true = Activo, false = Inactivo)", example = "true")
    private Boolean estado;
}
