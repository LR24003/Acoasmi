package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de petición para la creación y registro de una nueva Factura / DTE")
public class FacturacionRequestDTO {

    @NotBlank(message = "El usuario de sesión de caja es obligatorio.")
    @Schema(description = "Nombre de usuario de la caja activa desde donde se emite la factura", example = "juancarlos.cajero")
    private String usuario;

    @Schema(description = "Número único del asociado receptor de la factura. Si se envía, el sistema recupera sus datos (DUI/NIT, nombre y email). Si es null, se procesa como Consumidor Final.", example = "10452")
    private Integer numeroAsociado;

    @NotBlank(message = "El tipo de DTE es obligatorio.")
    @Size(max = 50, message = "El tipo de DTE no puede exceder los 50 caracteres.")
    @Schema(description = "Tipo de Documento Tributario Electrónico (Hacienda)", example = "01", allowableValues = {"01", "03", "14"})
    private String tipoDte;

    @Schema(description = "Código de generación único del DTE (UUID). Opcional en desarrollo (si es null, el backend lo genera automáticamente).", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID codigoGeneracionUuid;

    @Size(max = 30, message = "El número de control no puede exceder los 30 caracteres.")
    @Schema(description = "Número de control asignado internamente para el DTE. Opcional en desarrollo (si es null, el backend lo genera automáticamente).", example = "DTE-01-C01-00000001")
    private String numeroControl;

    @NotBlank(message = "La forma de pago es obligatoria.")
    @Schema(description = "Forma de pago de la factura", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA", "DESCUENTO_DESEMBOLSO", "CHEQUE"})
    private String formaPago = "EFECTIVO";

    @Schema(description = "Moneda de la transacción", example = "USD")
    private String moneda = "USD";

    @NotNull(message = "El monto gravado es obligatorio.")
    @PositiveOrZero(message = "El monto gravado debe ser mayor o igual a cero.")
    @Schema(description = "Monto de la operación sujeto a IVA", example = "100.00")
    private BigDecimal montoGravado;

    @NotNull(message = "El monto exento es obligatorio.")
    @PositiveOrZero(message = "El monto exento debe ser mayor o igual a cero.")
    @Schema(description = "Monto libre de IVA", example = "0.00")
    private BigDecimal montoExento;

    @NotNull(message = "El monto no sujeto es obligatorio.")
    @PositiveOrZero(message = "El monto no sujeto debe ser mayor o igual a cero.")
    @Schema(description = "Monto no sujeto a impuestos", example = "0.00")
    private BigDecimal montoNoSujeto = BigDecimal.ZERO;

    @NotNull(message = "El monto subtotal es obligatorio.")
    @PositiveOrZero(message = "El monto subtotal debe ser mayor o igual a cero.")
    @Schema(description = "Suma de los montos antes de impuestos y descuentos", example = "100.00")
    private BigDecimal montoSubtotal;

    @NotNull(message = "El monto descuento es obligatorio.")
    @PositiveOrZero(message = "El monto descuento debe ser mayor o igual a cero.")
    @Schema(description = "Monto total de descuentos aplicados", example = "0.00")
    private BigDecimal montoDescuento = BigDecimal.ZERO;

    @NotNull(message = "El monto IVA es obligatorio.")
    @PositiveOrZero(message = "El monto IVA debe ser mayor o igual a cero.")
    @Schema(description = "Cálculo del impuesto al valor agregado aplicado", example = "13.00")
    private BigDecimal montoIva;

    @NotNull(message = "El monto total es obligatorio.")
    @Positive(message = "El monto total debe ser estrictamente mayor a cero.")
    @Schema(description = "Suma final de los montos de la factura", example = "113.00")
    private BigDecimal montoTotal;

    @NotEmpty(message = "La factura debe incluir al menos un detalle o ítem.")
    @Valid
    @Schema(description = "Listado de ítems o servicios cobrados en la factura")
    private List<FacturaDetallesRequestDTO> detalles;
}