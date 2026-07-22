package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO que contiene la información requerida para la impresión del comprobante de caja de depósitos o retiros")
public class ComprobanteMovimientoDTO {

    @Schema(description = "numero correlativo del comprobante", example = "01-210938")
    private String numeroComprobante;

    @Schema(
            description = "Tipo de transacción realizada",
            example = "DEPÓSITO DE AHORRO",
            allowableValues = {"DEPÓSITO DE AHORRO", "RETIRO DE AHORRO", "DEPÓSITO DE APORTACIONES", "RETIRO DE APORTACIONES"}
    )
    private String tipoMovimiento;

    @Schema(description = "Número institucional único del asociado", example = "2045")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado que realiza o recibe el movimiento", example = "Juan Carlos Pérez Gómez")
    private String nombreCompletoAsociado;

    @Schema(description = "Monto procesado en la transacción", example = "150.00")
    private BigDecimal monto;

    @Schema(description = "Nombre de usuario del cajero que registró el movimiento", example = "José Perez")
    private String usuarioCajero;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Schema(
            description = "Fecha y hora exacta de cuándo se consolidó el movimiento en el servidor.",
            example = "20/07/2026 14:30:00"
    )    private LocalDateTime fechaMovimiento;

    @Schema(description = "Id del prestamo vinculado al movimiento", example = "1")
    private Long idPrestamo;

    @Schema(description = "Id de la factura (solo pagos de cuotas de prestamos)", example = "1")
    private Long idFacturaReferencia;
}