package com.acoasmi.roble.dto.request;

import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar una evaluación, aprobación o rechazo en el historial de decisiones del crédito")
public class HistorialAprobacionesRequestDTO {

    @NotNull(message = "El número de asociado es obligatorio")
    @Schema(description = "Correlativo único del asociado", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numeroAsociado;

    @NotBlank(message = "El número de solicitud es obligatorio")
    @Schema(description = "Número correlativo de la solicitud a evaluar", example = "SOLI-2026-0001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroSolicitud;

    @NotNull(message = "El estado resultante de la evaluación es obligatorio")
    @Schema(
            description = "Nuevo estado al que transiciona la solicitud según la resolución tomada",
            example = "EN_REVISION_GERENCIA",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EstadoSolicitudCredito estadoNuevo;

    @NotBlank(message = "El usuario responsable de la evaluación es obligatorio")
    @Schema(description = "Nombre de usuario del responsable (Asesor, Gerente, Miembro de Comité/Consejo) que registra la decisión", example = "jgarcia", requiredMode = Schema.RequiredMode.REQUIRED)
    private String usuarioResponsable;

    @NotBlank(message = "La descripción o justificación del dictamen es obligatoria")
    @Size(max = 1000, message = "La descripción de la resolución no puede exceder los 1000 caracteres")
    @Schema(
            description = "Resumen o detalle formal del dictamen o resolución tomada por el nivel decisor",
            example = "Solicitud evaluada favorablemente en sesión ordinaria de Gerencia General según Acta N° 12-2026.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String descripcionSolicitudCredito;

    @Size(max = 1000, message = "Las valoraciones no pueden exceder los 1000 caracteres")
    @Schema(
            description = "Valoraciones técnicas, financieras o de riesgo consideradas por el nivel decisor",
            example = "Capacidad de pago holgada. El asociado cuenta con garantía prendaria con cobertura del 120% del monto."
    )
    private String valoracionesNivelAprobacion;

    @Size(max = 1000, message = "Las recomendaciones no pueden exceder los 1000 caracteres")
    @Schema(
            description = "Recomendaciones o condiciones previas al desembolso dictaminadas en este nivel",
            example = "Se recomienda requerir la firma del cónyuge como codeudor solidario antes de emitir la resolución final."
    )
    private String recomendacionesNivelAprobacion;
}