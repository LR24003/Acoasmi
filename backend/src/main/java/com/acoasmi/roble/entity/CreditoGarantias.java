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
@Table(name = "solicitudes_credito_garantias")
@AttributeOverride(name = "id", column = @Column(name = "id_garantia"))
public class CreditoGarantias extends AcoasmiEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito solicitudCredito;

    @Column(name = "tipo_garantia", nullable = false, length = 50)
    private String tipoGarantia;

    @Column(name = "valor_estimado", precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @Column(name = "direccion_garantia", nullable = false, length = 150)
    private String direccionGarantia;

    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @Column(name = "nombre_fiador", length = 150)
    private String nombreFiador;

    @Column(name = "identificacion_fiador", length = 30)
    private String identificacionFiador;

    @Column(name = "telefono_fiador", length = 20)
    private String telefonoFiador;

    @Column(name = "ingresos_fiador", precision = 12, scale = 2)
    private BigDecimal ingresosFiador;

}
