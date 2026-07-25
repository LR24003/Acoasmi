package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con el detalle completo de la solicitud de crédito")
public class SolicitudesCreditoResponseDTO {

    @Schema(description = "ID único de la solicitud de crédito", example = "88")
    private Long idSolicitudLinea;

    @Schema(description = "Número único correlativo o código asignado al préstamo/solicitud", example = "PR-2026-0088")
    private String numeroSolicitud;

    @Schema(description = "Nombre completo del Asociado", example = "José Mendez Perez")
    private String nombreCompletoAsociado;

    @Schema(description = "Monto de dinero solicitado en el crédito", example = "5000.00")
    private BigDecimal montoSolicitado;

    @Schema(description = "Plazo de financiamiento expresado en meses", example = "24")
    private Integer plazoMeses;

    @Schema(description = "Tasa de interés anual de referencia según la línea de crédito", example = "18.00")
    private BigDecimal tasaReferencia;

    @Schema(description = "Destino puntual del crédito", example = "Solicitud para consolidación de deudas comerciales.")
    private String destinoCredito;

    @Schema(description = "Estado actual de la solicitud de crédito", example = "PENDIENTE")
    private String estadoPrestamo;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Evaluación y detalle analítico del crédito realizado por el asesor")
    private CreditoDetallesResponseDTO detallesEvaluacion;

    @Builder.Default
    @Schema(description = "Lista de garantías vinculadas a la solicitud")
    private List<SolicitudGarantiaRelacionResponseDTO> garantias = new ArrayList<>();

    @Builder.Default
    @Schema(description = "Lista de referencias personales/familiares asociadas")
    private List<CreditoReferenciasResponseDTO> referencias = new ArrayList<>();

    @Builder.Default
    @Schema(description = "Listado de documentos adjuntos al expediente de la solicitud")
    private List<CreditoDocumentosAdjuntosResponseDTO> documentosAdjuntos = new ArrayList<>();

    @Schema(description = "El estado en el que se encuentra la solicitud de credito en la BD", example = "true")
    private Boolean estado;

    @Schema(description = "Nombre del asesor de créditos que gestiona la solicitud", example = "Jose Perez")
    private String usuarioAsesor;

}
