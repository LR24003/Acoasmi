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
@Schema(description = "DTO de respuesta con el detalle de una evaluación o dictamen registrado en el historial de la solicitud de crédito")
public class HistorialAprobacionesResponseDTO {

    @Schema(description = "Identificador único del registro de historial", example = "101")
    private Long idHistorialAprob;

    @Schema(description = "Correlativo único del asociado", example = "1001")
    private Integer numeroAsociado;

    @Schema(description = "Número correlativo de la solicitud evaluada", example = "SOLI-2026-0001")
    private String numeroSolicitud;

    @Schema(description = "Nombre del asociado que solicito el credito", example = "José Mendez Perez")
    private String nombreCompletoAsociado;

    @Schema(description = "Estado previo de la solicitud antes de procesar esta evaluación", example = "EN_ANALISIS_ASESOR")
    private String estadoAnterior;

    @Schema(description = "Nuevo estado de la solicitud tras el dictamen de este nivel", example = "APROBADA_GERENCIA")
    private String estadoNuevo;

    @Schema(description = "Nombre del usuario evaluador (Asesor, Gerente, Miembro de Comité) que registró el dictamen", example = "8")
    private String usuarioResponsable;

    @Schema(
            description = "Descripción o justificación formal del dictamen/resolución emitida",
            example = "Solicitud evaluada favorablemente en sesión ordinaria de Gerencia General según Acta N° 12-2026."
    )
    private String descripcionSolicitudCredito;

    @Schema(
            description = "Valoraciones técnicas, financieras o crediticias consideradas",
            example = "Capacidad de pago holgada. El asociado cuenta con garantía prendaria con cobertura del 120% del monto."
    )
    private String valoracionesNivelAprobacion;

    @Schema(
            description = "Recomendaciones o condiciones dictaminadas previa emisión o desembolso",
            example = "Se recomienda requerir la firma del cónyuge como codeudor solidario antes de emitir la resolución final."
    )
    private String recomendacionesNivelAprobacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Fecha y hora exacta en la que se registró la evaluación", example = "01-08-2026 10:34:45")
    private LocalDateTime fechaAprobacion;
}
