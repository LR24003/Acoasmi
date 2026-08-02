package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = {"empresa", "caja", "asociado", "usuario", "partida", "detalles"})
@ToString(exclude = {"empresa", "caja", "asociado", "usuario", "partida", "detalles"})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "facturas")
@AttributeOverride(name = "id", column = @Column(name = "id_factura"))
public class Facturas extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private ConfiguracionEmpresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion_caja", nullable = false)
    private ControlCajas caja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asociado")
    private Asociados asociado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida")
    private PartidasContables partida;

    @Column(name = "tipo_dte", nullable = false, length = 50)
    private String tipoDte;

    @Column(name = "codigo_generacion_uuid", nullable = false, unique = true)
    private UUID codigoGeneracionUuid;

    @Column(name = "numero_control", nullable = false, unique = true, length = 30)
    private String numeroControl;

    @Column(name = "sello_recepcion_mh", length = 100)
    private String selloRecepcionMh;

    @Column(name = "monto_gravado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoGravado = BigDecimal.ZERO;

    @Column(name = "monto_exento", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoExento = BigDecimal.ZERO;

    @Column(name = "monto_iva", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoIva = BigDecimal.ZERO;

    @Column(name = "monto_no_sujeto", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoNoSujeto = BigDecimal.ZERO;

    @Column(name = "monto_subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSubtotal = BigDecimal.ZERO;

    @Column(name = "monto_descuento", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoDescuento = BigDecimal.ZERO;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Column(name = "monto_total_letras", nullable = false)
    private String montoTotalLetras;

    @Column(name = "forma_pago", nullable = false, length = 30)
    private String formaPago = "EFECTIVO";

    @Column(name = "moneda", nullable = false, length = 3)
    private String moneda = "USD";

    @Column(name = "fecha_emision", updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "estado_dte", nullable = false, length = 20)
    private String estadoDte = "PROCESADO";

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaDetalles> detalles = new ArrayList<>();

    public void addDetalle(FacturaDetalles detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }

    public void removeDetalle(FacturaDetalles detalle) {
        detalles.remove(detalle);
        detalle.setFactura(null);
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaEmision == null) {
            this.fechaEmision = LocalDateTime.now();
        }
        if (this.moneda == null) {
            this.moneda = "USD";
        }
        if (this.formaPago == null) {
            this.formaPago = "EFECTIVO";
        }
        if (this.estadoDte == null) {
            this.estadoDte = "PROCESADO";
        }
    }
}