package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solicitudes_credito_garantias_relacion")
@AttributeOverride(name = "id", column = @Column(name = "id_solicitud_garantia"))
public class SolicitudesGarantiaRelacion extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SolicitudesCredito solicitudCredito;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_garantia", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CreditoGarantias garantia;

    @Column(name = "monto_comprometido", precision = 12, scale = 2)
    private BigDecimal montoComprometido;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;
}