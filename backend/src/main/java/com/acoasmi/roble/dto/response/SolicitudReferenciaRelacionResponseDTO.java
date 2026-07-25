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
@Schema(description = "DTO de respuesta para la relación entre la solicitud de crédito y la referencia vinculada")
public class SolicitudReferenciaRelacionResponseDTO {

    @Schema(description = "ID único del registro de relación (tabla pivote/vinculación)", example = "85")
    private Long idSolicitudReferencia;

    @Schema(description = "Parentesco o vínculo respecto al solicitante en esta solicitud", example = "Hermano/a")
    private String parentescoRelacion;

    @Schema(description = "Información detallada de la referencia asociada")
    private CreditoReferenciasResponseDTO datosReferencia;
}
