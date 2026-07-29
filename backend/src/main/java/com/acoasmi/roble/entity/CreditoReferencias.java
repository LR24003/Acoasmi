package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solicitudes_credito_referencias")
@AttributeOverride(name = "id", column = @Column(name = "id_referencia"))
public class CreditoReferencias extends AcoasmiEntity {

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @ColumnDefault("'PERSONAL'")
    @Column(name = "tipo_referencia", length = 30)
    private String tipoReferencia;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SolicitudesCredito solicitudCredito;
}