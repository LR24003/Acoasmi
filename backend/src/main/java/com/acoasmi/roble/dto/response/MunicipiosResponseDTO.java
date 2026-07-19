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
@Schema(description = "DTO para enviar los datos de un municipio")
public class MunicipiosResponseDTO {

    @Schema(description = "ID del municipio en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Codigo del municipio según MH", example = "15")
    private Integer codigoMunicipio;

    @Schema(description = "Nombre del municipio según MH", example = "San Vicente Sur")
    private String nombreMunicipio;

    @Schema(description = "Nombre del departamento al que pertenece el municipio", example = "San Vicente")
    private String nombreDepartamento;

    @Schema(description = "Estado del municipio en la base de datos (true = Activo, false = Inactivo)", example = "true")
    private Boolean estado;

}
