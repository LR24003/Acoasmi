package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solicitudes_credito_detalle")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_detalle"))
public class CreditoDetalles extends AcoasmiEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SolicitudesCredito solicitudCredito;

    @Column(name = "descripcion_credito", columnDefinition = "TEXT")
    private String descripcionCredito;

    @Column(name = "valoracion_proyecto", columnDefinition = "TEXT")
    private String valoracionProyecto;

    @Column(name = "valoracion_asociado", nullable = false, columnDefinition = "TEXT")
    private String valoracionAsociado;

    @Column(name = "descripcion_garantia", columnDefinition = "TEXT")
    private String descripcionGarantia;

    @Column(name = "historial_creditos_previos", columnDefinition = "TEXT")
    private String historialCreditosPrevios;

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;

    @PrePersist
    protected void onCreate() {
        if (this.fechaEvaluacion == null) {
            this.fechaEvaluacion = LocalDateTime.now();
        }
    }
}