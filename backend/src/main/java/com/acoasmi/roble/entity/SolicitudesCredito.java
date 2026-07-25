package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitudes_credito_linea")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_linea"))
public class SolicitudesCredito extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asociado", nullable = false)
    private Asociados asociado;

    @Column(name = "numero_solicitud", unique = true, length = 30)
    private String numeroSolicitud;

    @Column(name = "monto_solicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tasa_referencia")
    private TasasPrestamos tasaReferencia;

    @Column(name = "estado_solicitud", nullable = false, length = 30)
    @Builder.Default
    private String estadoSolicitud = "RECIBIDA";

    @Column(name = "destino_credito", columnDefinition = "text")
    private String destinoCredito;

    @CreationTimestamp
    @Column(name = "fecha_solicitud", updatable = false)
    private LocalDateTime fechaSolicitud;

    @UpdateTimestamp
    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @OneToOne(mappedBy = "solicitudCredito", cascade = CascadeType.ALL)
    private CreditoDetalles creditoDetalle;

    @Builder.Default
    @OneToMany(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SolicitudesGarantiaRelacion> garantias = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SolicitudesCreditoRelacion> referencias = new HashSet<>();

    @OneToMany(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreditoDocumentosAdjuntos> documentosAdjuntos = new HashSet<>();

    @Column(name = "estado_prestamo", nullable = false, length = 50)
    private String estadoPrestamo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuarios usuarioAsesor;

}
