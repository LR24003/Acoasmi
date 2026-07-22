package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa la respuesta detallada de una Factura / DTE registrado exitosamente")
public class FacturacionResponseDTO {

    @Schema(description = "ID único de la factura generado por el sistema", example = "1")
    private Long idFactura;

    @Schema(description = "Nombre del usuario cajero que registró la operación", example = "juancarlos.cajero")
    private String usuarioCajero;

    @Schema(description = "Número institucional o código único asignado al asociado (Nulo si es Consumidor Final)", example = "10452")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado receptor (o 'CONSUMIDOR FINAL')", example = "José Perez Mendez")
    private String nombreCompletoAsociado;

    @Schema(description = "Tipo de Documento Tributario Electrónico (Hacienda)", example = "01")
    private String tipoDte;

    @Schema(description = "Código de generación único del DTE (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID codigoGeneracionUuid;

    @Schema(description = "Número de control correlativo asignado internamente", example = "DTE-01-00000001")
    private String numeroControl;

    @Schema(description = "Monto gravado de la factura", example = "100.00")
    private BigDecimal montoGravado;

    @Schema(description = "Monto exento de la factura", example = "0.00")
    private BigDecimal montoExento;

    @Schema(description = "Monto del IVA calculado", example = "13.00")
    private BigDecimal montoIva;

    @Schema(description = "Monto total final de la factura", example = "113.00")
    private BigDecimal montoTotal;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Schema(
            description = "Fecha y hora exacta de cuándo se consolidó la factura en el servidor.",
            example = "20/07/2026 14:30:00"
    )
    private LocalDateTime fechaEmision;
}