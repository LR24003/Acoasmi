package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con el detalle del respaldo de garantía asignado a la solicitud")
public class SolicitudGarantiaRelacionResponseDTO {

    @Schema(description = "ID único del registro de relación (tabla pivote)", example = "102")
    private Long idSolicitudGarantia;

    @Schema(description = "Monto específico del valor de la garantía asignado a esta solicitud", example = "5000.00")
    private BigDecimal montoComprometido;

    @Schema(description = "Observaciones particulares de esta garantía en la solicitud", example = "Respaldo sobre el total del crédito")
    private String observaciones;

    @Schema(description = "Información detallada de la garantía asociada")
    private CreditoGarantiasResponseDTO garantia;
}
