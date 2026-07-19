package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para enviar los datos de un distrito")
public class DistritosResponseDTO {

    @Schema(description = "ID del distrito en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Codigo del distrito según MH", example = "11")
    private Integer codigoDistrito;

    @Schema(description = "Nombre del distrito", example = "Tecoluca")
    private String nombreDistrito;

    @Schema(description = "Estado del distrito en la base de datos (true = Activa, false = Inactiva)", example = "true")
    private Boolean estado;

    @Schema(description = "Nombre del departamento al que pertenece", example = "San Vicente")
    private String nombreDepartamento;

    @Schema(description = "Nombre del municipio al que pertenece", example = "San Vicente Sur")
    private String nombreMunicipio;
}
