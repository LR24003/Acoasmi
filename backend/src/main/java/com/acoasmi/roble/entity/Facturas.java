package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "facturas")
@AttributeOverride(name = "id", column = @Column(name = "id_factura"))
public class Facturas extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion_caja", nullable = false)
    private ControlCajas caja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asociado")
    private Asociados asociado;

    @Column(name = "tipo_dte", nullable = false, length = 50)
    private String tipoDte;

    @Column(name = "codigo_generacion_uuid", nullable = false, unique = true)
    private UUID codigoGeneracionUuid;

    @Column(name = "numero_control", nullable = false, unique = true, length = 30)
    private String numeroControl;

    @Column(name = "monto_gravado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoGravado = BigDecimal.ZERO;

    @Column(name = "monto_exento", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoExento = BigDecimal.ZERO;

    @Column(name = "monto_iva", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoIva = BigDecimal.ZERO;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "fecha_emision", updatable = false)
    private LocalDateTime fechaEmision;

    @PrePersist
    protected void onCreate() {
        if (this.fechaEmision == null) {
            this.fechaEmision = LocalDateTime.now();
        }
    }
}