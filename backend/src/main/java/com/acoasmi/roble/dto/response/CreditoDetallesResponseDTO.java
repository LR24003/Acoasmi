package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con el detalle analítico y evaluación de la solicitud de crédito")
public class CreditoDetallesResponseDTO {

    @Schema(description = "ID único del detalle del crédito", example = "10")
    private Long idDetalle;

    @Schema(description = "Descripción detallada del destino o propósito del crédito solicitado", example = "Compra de maquinaria agrícola para incrementar la producción de grano básico.")
    private String descripcionCredito;

    @Schema(description = "Evaluación cualitativa/cuantitativa sobre la viabilidad del proyecto", example = "El proyecto presenta una rentabilidad estimada del 18% anual con retorno a mediano plazo.")
    private String valoracionProyecto;

    @Schema(description = "Análisis del perfil, capacidad de pago y comportamiento crediticio del asociado", example = "Asociado con excelente récord crediticio, liquidez adecuada y 5 años de antigüedad en la cooperativa.")
    private String valoracionAsociado;

    @Schema(description = "Análisis de suficiencia y condición de las garantías ofrecidas", example = "La garantía hipotecaria cubre un 140% del monto solicitado según el avalúo reciente.")
    private String descripcionGarantia;

    @Schema(description = "Resumen del historial de préstamos anteriores y su cumplimiento de pago", example = "Ha liquidado exitosamente 3 créditos previos en la institución sin presentar mora.")
    private String historialCreditosPrevios;

    @Schema(description = "Dictamen técnico y recomendaciones del analista de crédito", example = "Se recomienda la aprobación del crédito bajo las condiciones y desembolso único solicitados.")
    private String recomendaciones;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Fecha y hora en que se registró o actualizó la evaluación", example = "20-07-2026 14:30:00")
    private LocalDateTime fechaEvaluacion;
}
