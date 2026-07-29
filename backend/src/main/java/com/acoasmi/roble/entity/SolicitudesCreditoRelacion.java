package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solicitudes_credito_referencias_relacion")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_referencia"))
public class SolicitudesCreditoRelacion extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SolicitudesCredito solicitudCredito;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_referencia", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CreditoReferencias referencia;

    @Column(name = "parentesco_relacion", length = 50)
    private String parentescoRelacion;
}