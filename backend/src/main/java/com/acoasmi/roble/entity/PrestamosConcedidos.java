package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"asociado", "credito"})
@EqualsAndHashCode(callSuper = true, exclude = {"asociado", "credito"})
@Table(name = "prestamos_concedidos")
@AttributeOverride(name = "id", column = @Column(name = "id_prestamo"))
public class PrestamosConcedidos extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asociado", nullable = false)
    private Asociados asociado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito credito;

    @Column(name = "numero_prestamo", nullable = false, length = 50, unique = true)
    private String numeroPrestamo;

    @Column(name = "monto_concedido", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoConcedido;

    @Column(name = "saldo_capital_actual", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoCapitalActual;

    @Column(name = "tasa_interes_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia_pago", nullable = false, length = 20)
    private FrecuenciaPago frecuenciaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_prestamo", nullable = false, length = 20)
    private EstadoPrestamo estadoPrestamo;

    @Column(name = "fecha_desembolso", nullable = false)
    private LocalDateTime fechaDesembolso;

    @Column(name = "tasa_mora_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaMoraAnual;

    @Column(name = "saldo_interes_pendiente", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoInteresPendiente;

    @Column(name = "saldo_mora_acumulada", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoMoraAcumulada;

    @Column(name = "monto_seguro_deuda", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSeguroDeuda;

    @Column(name = "monto_aportacion", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoAportacion;

    @Column(name = "monto_ahorro_simultaneo", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoAhorroSimultaneo;

    @Column(name = "monto_cuota_gestion", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoCuotaGestion;

    @Column(name = "fecha_proximo_pago")
    private LocalDate fechaProximoPago;

    @Column(name = "fecha_ultimo_pago")
    private LocalDate fechaUltimoPago;

    @Column(name = "dias_atraso", nullable = false)
    private Integer diasAtraso;

    public enum FrecuenciaPago {
        MENSUAL, BIMENSUAL, TRIMESTRAL, CUATRIMESTRAL, SEMESTRAL, ANUAL
    }

    public enum EstadoPrestamo {
        AL_DIA, EN_MORA, CANCELADO, SANEADO
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaDesembolso == null) {
            this.fechaDesembolso = LocalDateTime.now();
        }
    }
}