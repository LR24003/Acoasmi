package com.acoasmi.roble.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "municipios")
@AttributeOverride(name = "id", column = @Column(name = "id_municipio"))
public class Municipios extends AcoasmiEntity{

    @Column(name = "codigo_municipio", unique = true)
    private Integer codigoMunicipio;

    @Column(name = "nombre_municipio", nullable = false, length = 100)
    private String nombreMunicipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo_departamento", nullable = false)
    private Departamentos departamento;

    @JsonFormat(pattern = "%02d")
    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }
}
