package com.acoasmi.roble.entity;

import com.acoasmi.roble.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"asociado", "prestamo", "solicitud", "factura", "cuenta", "deducciones"})
@EqualsAndHashCode(callSuper = true, exclude = {"asociado", "prestamo", "solicitud", "factura", "cuenta", "deducciones"})
@Table(name = "desembolsos_credito")
@AttributeOverride(name = "id", column = @Column(name = "id_desembolso"))
public class DesembolsoCredito extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sesion_caja", nullable = false)
    private ControlCajas caja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asociado", nullable = false)
    private Asociados asociado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prestamo", nullable = false)
    private PrestamosConcedidos prestamo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito solicitud;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_factura", nullable = false)
    private Facturas factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta")
    private AsociadoCuentas cuenta;

    @Column(name = "numero_solicitud", length = 20)
    private String numeroSolicitud;

    @Column(name = "numero_desembolso", length = 20, nullable = false)
    private String numeroDesembolso;

    @Column(name = "monto_bruto_desembolso", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoBrutoDesembolso;

    @Column(name = "total_deducciones", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalDeducciones;

    @Column(name = "monto_neto_entregado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoNetoEntregado;

    @Column(name = "fecha_desembolso", nullable = false)
    private LocalDateTime fechaDesembolso;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false, length = 30)
    private FormaPago formaPago;

    @Column(name = "numero_cuenta_destino", length = 50)
    private String numeroCuentaDestino;

    @Column(name = "numero_comprobante", length = 50)
    private String numeroComprobante;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Builder.Default
    @OneToMany(mappedBy = "desembolso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesembolsoDeducciones> deducciones = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    @PrePersist
    protected void onCreate() {
        if (this.fechaDesembolso == null) {
            this.fechaDesembolso = LocalDateTime.now();
        }
    }

    public void addDeduccion(DesembolsoDeducciones deduccion) {
        deducciones.add(deduccion);
        deduccion.setDesembolso(this);
    }

    public void removeDeduccion(DesembolsoDeducciones deduccion) {
        deducciones.remove(deduccion);
        deduccion.setDesembolso(null);
    }
}