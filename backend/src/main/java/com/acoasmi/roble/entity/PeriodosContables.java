package com.acoasmi.roble.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "periodos_contables")
@AttributeOverride(name = "id", column = @Column(name = "id_periodo"))
public class PeriodosContables extends AcoasmiEntity{

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Size(max = 15)
    @ColumnDefault("'ABIERTO'")
    @Column(name = "estado_periodo", nullable = false, length = 15)
    private String estadoPeriodo;
}
