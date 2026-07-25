package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitudes_credito_garantias_relacion")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_garantia"))
public class SolicitudesGarantiaRelacion extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito solicitudCredito;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_garantia", nullable = false)
    private CreditoGarantias garantia;

    @Column(name = "monto_comprometido", precision = 12, scale = 2)
    private BigDecimal montoComprometido;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;
}