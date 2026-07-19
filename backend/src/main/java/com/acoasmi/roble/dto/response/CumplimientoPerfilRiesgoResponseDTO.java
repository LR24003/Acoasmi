package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CumplimientoPerfilRiesgoResponseDTO {

    @Schema(description = "Id unico del perfil de riesgo", example = "1")
    private Long id;

    @Schema(description = "Id del asociado", example = "1")
    private Long idAsociado;

    @Schema(description = "Número correlativo único del asociado", example = "1850")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado", example = "José Alfredo López Rivera")
    private String nombreCompletoAsociado;

    @Schema(description = "Nivel del riesgo que representa el asociado según el análisis", example = "BAJO")
    private String nivelRiesgo;

    @Schema(description = "Si es PEP = true, No es PEP = false", example = "true")
    private Boolean esPep;

    @Schema(description ="Detalla si el asociado a ocupado un cargo publico o es familia de un PEP", example = "Diputado, Alcalde")
    private String cargoOrigenPep;

    @Schema(description ="Monton maximo en dinero a manejar por parte del asociado", example = "600.00")
    private BigDecimal montoMaximoMensualEsperado;

    @Schema(description ="Origen de los fondos del asociado, según en lo que trabaja", example = "Salario por empleo en empresa privada")
    private String origenFondosDeclarado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Fecha de la ultima actualización del perfil de riesgo del asociado", example = "18/07/2026 13:30")
    private LocalDateTime fechaUltimaActualizacion;

    @Schema(description = "Estado actual del perfil de riesgo del asociado", example = "true")
    private Boolean estado;
}
