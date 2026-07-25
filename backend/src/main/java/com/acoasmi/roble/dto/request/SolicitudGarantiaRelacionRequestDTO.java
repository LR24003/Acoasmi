package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para vincular una garantía a la solicitud de crédito especificando el monto comprometido")
public class SolicitudGarantiaRelacionRequestDTO {

    @DecimalMin(value = "0.00", message = "El monto comprometido no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El monto comprometido debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto específico del valor de la garantía que respaldará esta solicitud en particular", example = "5000.00")
    private BigDecimal montoComprometido;

    @Schema(description = "Observaciones o condiciones específicas del respaldo ofrecido en esta solicitud", example = "Garantía compartida en primer grado con el crédito previo PR-2025-012.")
    private String observaciones;

    @Valid
    @Schema(description = "Datos completos de la garantía (Obligatorio solo si es una garantía NUEVA)")
    private CreditoGarantiasRequestDTO datosGarantia;
}