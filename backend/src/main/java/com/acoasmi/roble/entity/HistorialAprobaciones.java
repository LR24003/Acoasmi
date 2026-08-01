package com.acoasmi.roble.entity;

import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historial_aprobaciones")
@AttributeOverride(name = "id", column = @Column(name = "id_historial_aprob"))
public class HistorialAprobaciones extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito solicitudCredito;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudCredito estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudCredito estadoNuevo;

    @Column(name = "descripcion_solicitud_credito", length = 1000, nullable = false)
    private String descripcionSolicitudCredito;

    @Column(name = "valoraciones_nivel_aprobacion", length = 1000, nullable = false)
    private String valoracionesNivelAprobacion;

    @Column(name = "recomendaciones_nivel_aprobacion", length = 1000,  nullable = false)
    private String recomendacionesNivelAprobacion;

    private LocalDateTime fechaAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuarioResponsable;

    @PrePersist
    public void prePersist() {
        this.fechaAprobacion = LocalDateTime.now();
    }
    
}
