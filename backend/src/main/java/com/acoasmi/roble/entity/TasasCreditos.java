package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "tasas_prestamos")
@AttributeOverride(name = "id", column = @Column(name = "id_tasa"))
public class TasasCreditos extends AcoasmiEntity {

    @Size(max = 100)
    @NotNull(message = "El nombre del producto es obligatorio")
    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @NotNull
    @Column(name = "tasa_interes_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tasas_frecuencias_pago",
            joinColumns = @JoinColumn(name = "tasa_credito_id")
    )
    @Column(name = "frecuencia_pago", nullable = false, length = 20)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<String> frecuenciasPago = new HashSet<>();
}