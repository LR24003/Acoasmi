package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "control_cajas")
@AttributeOverride(name = "id", column = @Column(name = "id_sesion_caja"))
public class ControlCajas extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_cajero", nullable = false)
    private Usuarios usuarioCajero;

    @Column(name = "monto_apertura", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoApertura;

    @Column(name = "monto_cierre_teorico", precision = 12, scale = 2)
    private BigDecimal montoCierreTeorico;

    @Column(name = "monto_cierre_real", precision = 12, scale = 2)
    private BigDecimal montoCierreReal;

    @Column(name = "fecha_apertura", updatable = false)
    private ZonedDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private ZonedDateTime fechaCierre;

    @PrePersist
    protected void onCreateCaja() {
        if (this.fechaApertura == null) {
            this.fechaApertura = ZonedDateTime.now();
        }
        if (this.montoCierreTeorico == null) {
            this.montoCierreTeorico = BigDecimal.ZERO;
        }
        if (this.montoCierreReal == null) {
            this.montoCierreReal = BigDecimal.ZERO;
        }
        this.setEstado(true);
    }
}
