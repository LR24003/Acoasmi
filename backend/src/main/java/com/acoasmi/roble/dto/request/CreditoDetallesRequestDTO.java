package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación o actualización de la evaluación y detalle analítico de la solicitud de crédito")
public class CreditoDetallesRequestDTO {

    @NotBlank(message = "La Descripción del credito o proyecto es obligatorio")
    @Schema(description = "Descripción detallada del destino o propósito del crédito solicitado", example = "Compra de maquinaria agrícola para incrementar la producción de grano básico.")
    private String descripcionCredito;

    @Schema(description = "Evaluación cualitativa/cuantitativa sobre la viabilidad del proyecto", example = "El proyecto presenta una rentabilidad estimada del 18% anual con retorno a mediano plazo.")
    private String valoracionProyecto;

    @NotBlank(message = "La valoración del asociado es obligatoria")
    @Schema(description = "Análisis del perfil, capacidad de pago y comportamiento crediticio del asociado", example = "Asociado con excelente récord crediticio, liquidez adecuada y 5 años de antigüedad en la cooperativa.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String valoracionAsociado;

    @NotBlank(message = "La descripción de la garantia ofertada por el asociado es obligatoria")
    @Schema(description = "Análisis de suficiencia y condición de las garantías ofrecidas", example = "La garantía hipotecaria cubre un 140% del monto solicitado según el avalúo reciente.")
    private String descripcionGarantia;

    @NotBlank(message = "El resumen del historial del credito es obligatoria(aunque no posea record)")
    @Schema(description = "Resumen del historial de préstamos anteriores y su cumplimiento de pago", example = "Ha liquidado exitosamente 3 créditos previos en la institución sin presentar mora.")
    private String historialCreditosPrevios;

    @NotBlank(message = "Las recomendaciones del asesor son obligatorias")
    @Schema(description = "Dictamen técnico y recomendaciones del analista de crédito", example = "Se recomienda la aprobación del crédito bajo las condiciones y desembolso único solicitados.")
    private String recomendaciones;
}