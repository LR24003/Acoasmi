package com.acoasmi.roble.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departamentos")
@AttributeOverride(name = "id", column = @Column(name = "id_departamento"))
public class Departamentos extends AcoasmiEntity{

    @Column(name = "codigo_departamento", unique = true)
    private Integer codigoDepartamento;

    @Column(name = "nombre_departamento", nullable = false, length = 100)
    private String nombreDepartamento;

    @JsonFormat(pattern = "%02d")
    public Integer getCodigoDepartamento() {
        return codigoDepartamento;
    }

}
