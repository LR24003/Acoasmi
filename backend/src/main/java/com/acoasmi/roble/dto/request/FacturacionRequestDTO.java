package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de petición para la creación y registro de una nueva Factura / DTE")
public class FacturacionRequestDTO {

    @NotBlank(message = "El usuario de sesión de caja es obligatorio.")
    @Schema(description = "Nombre de usuario de la caja activa desde donde se emite la factura", example = "juancarlos.cajero")
    private String usuario;

    @Schema(description = "Nombre completo del asociado receptor de la factura (Opcional si es Consumidor Final)", example = "José Perez Mendez")
    private String nombreCompletoAsociado;

    @NotBlank(message = "El tipo de DTE es obligatorio.")
    @Size(max = 50, message = "El tipo de DTE no puede exceder los 50 caracteres.")
    @Schema(description = "Tipo de Documento Tributario Electrónico (Hacienda)", example = "01", allowableValues = {"01", "03", "14"})
    private String tipoDte;

    @NotNull(message = "El código de generación UUID es obligatorio.")
    @Schema(description = "Código de generación único del DTE (UUID provisto por el sistema emisor/Hacienda)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID codigoGeneracionUuid;

    @NotBlank(message = "El número de control es obligatorio.")
    @Size(max = 30, message = "El número de control no puede exceder los 30 caracteres.")
    @Schema(description = "Número de control asignado internamente para el seguimiento del DTE", example = "DTE-01-00000001")
    private String numeroControl;

    @NotNull(message = "El monto gravado es obligatorio.")
    @PositiveOrZero(message = "El monto gravado debe ser mayor o igual a cero.")
    @Schema(description = "Monto de la operación sujeto a impuestos", example = "100.00")
    private BigDecimal montoGravado;

    @NotNull(message = "El monto exento es obligatorio.")
    @PositiveOrZero(message = "El monto exento debe ser mayor o igual a cero.")
    @Schema(description = "Monto libre de retenciones o IVA", example = "0.00")
    private BigDecimal montoExento;

    @NotNull(message = "El monto IVA es obligatorio.")
    @PositiveOrZero(message = "El monto IVA debe ser mayor o igual a cero.")
    @Schema(description = "Cálculo del impuesto al valor agregado aplicado", example = "13.00")
    private BigDecimal montoIva;

    @NotNull(message = "El monto total es obligatorio.")
    @Positive(message = "El monto total debe ser estrictamente mayor a cero.")
    @Schema(description = "Suma final de los montos de la factura", example = "113.00")
    private BigDecimal montoTotal;
}