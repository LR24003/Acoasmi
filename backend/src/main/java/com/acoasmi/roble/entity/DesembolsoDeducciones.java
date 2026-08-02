package com.acoasmi.roble.entity;

import com.acoasmi.roble.enums.TipoDeduccion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "desembolso")
@EqualsAndHashCode(callSuper = true, exclude = "desembolso")
@Table(name = "desembolso_deducciones")
@AttributeOverride(name = "id", column = @Column(name = "id_deduccion"))
public class DesembolsoDeducciones extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_desembolso", nullable = false)
    private DesembolsoCredito desembolso;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_deduccion", nullable = false, length = 40)
    private TipoDeduccion tipoDeduccion;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "descripcion")
    private String descripcion;


}
