package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa un ítem o detalle individual de la factura / DTE")
public class FacturaDetallesResponseDTO {

    @Schema(description = "ID único del detalle generado por el sistema", example = "101")
    private Long id;

    @Schema(description = "Número correlativo de la línea dentro de la factura", example = "1")
    private Integer numItem;

    @Schema(description = "Tipo de ítem (1: Bien, 2: Servicio, 3: Ambos)", example = "2")
    private Integer tipoItem;

    @Schema(description = "Código interno del producto o servicio", example = "SER-001")
    private String codigoProducto;

    @Schema(description = "Descripción detallada del bien o servicio prestado", example = "Cobro por Gastos Administrativos")
    private String descripcion;

    @Schema(description = "Cantidad del producto o servicio", example = "1.0000")
    private BigDecimal cantidad;

    @Schema(description = "Precio unitario sin IVA aplicado", example = "100.0000")
    private BigDecimal precioUnitario;

    @Schema(description = "Monto de descuento aplicado a esta línea", example = "0.00")
    private BigDecimal montoDescuento;

    @Schema(description = "Monto subtotal de la línea de venta", example = "100.00")
    private BigDecimal montoVenta;

    @Schema(description = "Monto de IVA correspondiente a esta línea", example = "13.00")
    private BigDecimal montoIvaItem;

    @Schema(description = "Código de la cuenta contable asociada en el catálogo", example = "51010101")
    private String codigoCuentaContable;

    @Schema(description = "Nombre de la cuenta contable asignada para la partida automática", example = "Ingresos por Servicios Administrativos")
    private String nombreCuentaContable;
}
