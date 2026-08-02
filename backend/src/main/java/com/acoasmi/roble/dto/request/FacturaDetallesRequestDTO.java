package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la recepción de ítems o líneas de detalle de una factura")
public class FacturaDetallesRequestDTO {

    @NotNull(message = "El número de ítem es obligatorio.")
    @Min(value = 1, message = "El número de ítem debe ser mayor o igual a 1.")
    @Schema(description = "Correlativo de la línea de detalle", example = "1")
    private Integer numItem;

    @NotNull(message = "El tipo de ítem es obligatorio.")
    @Schema(description = "Tipo de ítem (1: Bien, 2: Servicio, 3: Ambos)", example = "2")
    private Integer tipoItem = 2;

    @Schema(description = "Código interno del producto o servicio", example = "SER-001")
    private String codigoProducto;

    @NotBlank(message = "La descripción del ítem es obligatoria.")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres.")
    @Schema(description = "Descripción del bien o servicio cobrado", example = "Cobro por Gastos Administrativos")
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria.")
    @Positive(message = "La cantidad debe ser estrictamente mayor a cero.")
    @Schema(description = "Cantidad vendida o prestada", example = "1.0000")
    private BigDecimal cantidad;

    @NotNull(message = "El precio unitario es obligatorio.")
    @PositiveOrZero(message = "El precio unitario debe ser mayor o igual a cero.")
    @Schema(description = "Precio unitario sin IVA", example = "100.0000")
    private BigDecimal precioUnitario;

    @NotNull(message = "El monto de descuento del ítem es obligatorio.")
    @PositiveOrZero(message = "El descuento debe ser mayor o igual a cero.")
    @Schema(description = "Descuento aplicado a la línea", example = "0.00")
    private BigDecimal montoDescuento = BigDecimal.ZERO;

    @NotNull(message = "El monto venta del ítem es obligatorio.")
    @PositiveOrZero(message = "El monto venta debe ser mayor o igual a cero.")
    @Schema(description = "Subtotal de la línea de detalle", example = "100.00")
    private BigDecimal montoVenta;

    @NotNull(message = "El monto IVA del ítem es obligatorio.")
    @PositiveOrZero(message = "El monto IVA del ítem debe ser mayor o igual a cero.")
    @Schema(description = "Monto de IVA asociado a este ítem", example = "13.00")
    private BigDecimal montoIvaItem;

    @Schema(description = "ID de la cuenta contable asociada para la partida automática (Opcional)", example = "15")
    private Long idCuentaContable;
}