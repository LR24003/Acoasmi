package com.acoasmi.roble.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitudes_credito_referencias")
@AttributeOverride(name = "id", column = @Column(name = "id_referencia"))
public class CreditoReferencias extends AcoasmiEntity {

    @ColumnDefault("'PERSONAL'")
    @Column(name = "tipo_referencia", length = 30)
    private String tipoReferencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito  solicitudCredito;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;
    
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "direccion", nullable = false)
    private String direccion;
}
