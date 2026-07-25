package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la transferencia de datos para la creación de una solicitud de crédito")
public class SolicitudesCreditoRequestDTO {

    @Size(max = 30, message = "El número de préstamo no debe exceder 30 caracteres")
    @Schema(description = "Número o código correlativo asignado a la solicitud", example = "SOLI-01-0001")
    private String numeroSolicitud;

    @NotBlank(message = "El nombre del asociado es obligatorio")
    @Schema(description = "Nombre completo del Asociado", example = "José Mendez Perez")
    private String nombreCompletoAsociado;

    @NotNull(message = "El monto solicitado es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto solicitado debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto de dinero solicitado en el crédito", example = "5000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal montoSolicitado;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es de 1 mes")
    @Max(value = 360, message = "El plazo máximo permitido es de 360 meses (30 años)")
    @Schema(description = "Plazo de financiamiento expresado en meses", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer plazoMeses;

    @NotNull(message = "La tasa de interés es obligatoria")
    @Schema(description = "Tasa de interés anual de referencia según la línea de crédito", example = "18.00")
    private BigDecimal tasaReferencia;

    @Size(max = 50, message = "El destino del crédito no puede sobrepasar los 50 caracteres")
    @Schema(description = "Destino puntual del crédito", example = "Solicitud para consolidación de deudas comerciales.")
    private String destinoCredito;

    @Valid
    @Schema(description = "Evaluación cualitativa, análisis de riesgo y recomendación técnica del asesor")
    private CreditoDetallesRequestDTO analisisAsesor;

    @Valid
    @Builder.Default
    @Schema(description = "Lista de garantías asociadas a la solicitud")
    private List<CreditoGarantiasRequestDTO> garantias = new ArrayList<>();

    @Valid
    @Builder.Default
    @Schema(description = "Lista de referencias personales/familiares asociadas")
    private List<CreditoReferenciasRequestDTO> referencias = new ArrayList<>();

    @Valid
    @Builder.Default
    @Schema(description = "Lista de documentos adjuntos que respaldan la solicitud")
    private List<CreditoDocumentosAdjuntosRequestDTO> documentosAdjuntos = new ArrayList<>();

    @NotBlank(message = "El estado del proceso del préstamo es obligatorio")
    @Schema(description = "El estado del proceso en el que se encuentra la solicitud de crédito", example = "PENDIENTE")
    private String estadoPrestamo;

    @Schema(description = "Nombre del asesor de créditos que gestiona la solicitud", example = "Jose Perez")
    private String usuarioAsesor;
}