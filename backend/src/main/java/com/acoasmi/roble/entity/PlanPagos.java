package com.acoasmi.roble.entity;

import com.acoasmi.roble.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "prestamo")
@EqualsAndHashCode(callSuper = true, exclude = "prestamo")
@Table(name = "prestamos_proyeccion_cuotas")
@AttributeOverride(name = "id", column = @Column(name = "id_proyeccion"))
public class PlanPagos extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prestamo", nullable = false)
    private PrestamosConcedidos prestamo;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;

    @Column(name = "fecha_vencimiento_proyectada", nullable = false)
    private LocalDate fechaVencimientoProyectada;

    @Column(name = "monto_cuota_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoCuotaBase;

    @Column(name = "abono_capital", nullable = false, precision = 12, scale = 2)
    private BigDecimal abonoCapital;

    @Column(name = "abono_interes", nullable = false, precision = 12, scale = 2)
    private BigDecimal abonoInteres;

    @Column(name = "saldo_capital_restante", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoCapitalRestante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuota", nullable = false, length = 20)
    private EstadoCuota estadoCuota;

    @Column(name = "seguro_programado", nullable = false, precision = 12, scale = 2)
    private BigDecimal seguroProgramado;

    @Column(name = "aportacion_programada", nullable = false, precision = 12, scale = 2)
    private BigDecimal aportacionProgramada;

    @Column(name = "ahorro_programado", nullable = false, precision = 12, scale = 2)
    private BigDecimal ahorroProgramado;

    @Column(name = "cuota_gestion_programada", nullable = false, precision = 12, scale = 2)
    private BigDecimal cuotaGestionProgramada;

    @Column(name = "total_cuota", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCuota;

    @Column(name = "saldo_cuota_pendiente", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoCuotaPendiente;

}