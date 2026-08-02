package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true, exclude = {"factura", "cuentaContable"})
@ToString(exclude = {"factura", "cuentaContable"})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "factura_detalles")
@AttributeOverride(name = "id", column = @Column(name = "id_factura_detalle"))
public class FacturaDetalles extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    private Facturas factura;

    @Column(name = "num_item", nullable = false)
    private Integer numItem;

    @Column(name = "tipo_item", nullable = false)
    private Integer tipoItem = 1;

    @Column(name = "codigo_producto", length = 50)
    private String codigoProducto;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "cantidad", nullable = false, precision = 12, scale = 4)
    private BigDecimal cantidad = BigDecimal.ONE;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 4)
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    @Column(name = "monto_descuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoDescuento = BigDecimal.ZERO;

    @Column(name = "monto_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoVenta = BigDecimal.ZERO;

    @Column(name = "monto_iva_item", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoIvaItem = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta_contable")
    private CatalogoCuentas cuentaContable;
}