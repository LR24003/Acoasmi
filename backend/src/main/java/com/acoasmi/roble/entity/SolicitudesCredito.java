package com.acoasmi.roble.entity;

import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solicitudes_credito_linea")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_linea"))
public class SolicitudesCredito extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asociado", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Asociados asociado;

    @Column(name = "numero_solicitud", unique = true, length = 30)
    private String numeroSolicitud;

    @Column(name = "monto_solicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tasa_referencia")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TasasCreditos tasaReferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud", nullable = false)
    private EstadoSolicitudCredito estadoActual;

    @Column(name = "destino_credito", columnDefinition = "text")
    private String destinoCredito;

    @CreationTimestamp
    @Column(name = "fecha_solicitud", updatable = false)
    private LocalDateTime fechaSolicitud;

    @UpdateTimestamp
    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @OneToOne(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CreditoDetalles creditoDetalle;

    @Builder.Default
    @OneToMany(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SolicitudesGarantiaRelacion> garantias = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SolicitudesCreditoRelacion> referencias = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "solicitudCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CreditoDocumentosAdjuntos> documentosAdjuntos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuarios usuarioAsesor;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_solicitud_linea")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<HistorialAprobaciones> historialAprobaciones = new ArrayList<>();
}