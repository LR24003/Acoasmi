package com.acoasmi.roble.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitudes_credito_detalle")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_detalle"))
public class CreditoDetalles extends AcoasmiEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito solicitudCredito;

    @Column(name = "descripcion_credito", length = Integer.MAX_VALUE)
    private String descripcionCredito;

    @Column(name = "valoracion_proyecto", length = Integer.MAX_VALUE)
    private String valoracionProyecto;

    @Column(name = "valoracion_asociado", nullable = false, length = Integer.MAX_VALUE)
    private String valoracionAsociado;

    @Column(name = "descripcion_garantia", length = Integer.MAX_VALUE)
    private String descripcionGarantia;

    @Column(name = "historial_creditos_previos", length = Integer.MAX_VALUE)
    private String historialCreditosPrevios;

    @Column(name = "recomendaciones", length = Integer.MAX_VALUE)
    private String recomendaciones;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;

    @PrePersist
    protected void onCreate() {
        this.fechaEvaluacion = LocalDateTime.now();
    }
}
