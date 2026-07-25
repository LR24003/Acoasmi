package com.acoasmi.roble.entity;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasas_prestamos")
@AttributeOverride(name = "id", column = @Column(name = "id_tasa"))
public class TasasPrestamos extends AcoasmiEntity{

    @Size(max = 100)
    @NotNull(message = "El nombre del producto es obligatorio")
    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @NotNull
    @Column(name = "tasa_interes_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    @Size(max = 20)
    @NotNull(message = "La frecuencia de pago es obligatoria")
    @Column(name = "frecuencia_pago", nullable = false, length = 20)
    private String frecuenciaPago;
}
