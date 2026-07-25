package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitudes_credito_referencias_relacion")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_referencia"))
public class SolicitudesCreditoRelacion extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito solicitudCredito;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_referencia", nullable = false)
    private CreditoReferencias referencia;

    @Column(name = "parentesco_relacion", length = 50)
    private String parentescoRelacion;
}