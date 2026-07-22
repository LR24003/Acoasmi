package com.acoasmi.roble.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asociado_movimientos")
@AttributeOverride(name = "id", column = @Column(name = "id_movimiento"))
public class AsociadoMovimientos extends AcoasmiEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion_caja")
    private ControlCajas caja;

    @Column(name = "numero_comprobante", length = 50)
    private String numeroComprobante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta")
    private AsociadoCuentas cuenta;

    @Column(name = "id_prestamo")
    private Long idPrestamo;

    @Column(name = "id_factura_referencia")
    private Long idFacturaReferencia;

    @Column(name = "tipo_movimiento", nullable = false, length = 30)
    private String tipoMovimiento;

    @Column(name ="monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "saldo_resultante", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoResultante;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_movimiento", insertable = false, updatable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "descripcion_movimiento", nullable = false, columnDefinition = "TEXT")
    private String descripcionMovimiento;

    @Column(name = "cambio_tasa", precision = 5, scale = 2)
    private BigDecimal cambioTasa = BigDecimal.ZERO;

    @PrePersist
    protected void onCreate() {
        if (this.fechaMovimiento == null) {
            this.fechaMovimiento = LocalDateTime.now();
        }
    }
}
