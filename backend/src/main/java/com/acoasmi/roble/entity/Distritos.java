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
@Table(name = "distritos")
@AttributeOverride(name = "id", column = @Column(name = "id_distrito"))
public class Distritos extends AcoasmiEntity{

    @Column(name = "codigo_distrito", nullable = false)
    @JsonFormat(pattern = "%02d")
    private Integer codigoDistrito;

    @Column(name = "nombre_distrito", nullable = false, length = 100)
    private String nombreDistrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo_departamento", nullable = false)
    private Departamentos departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_municipio", referencedColumnName = "codigo_municipio", nullable = false)
    private Municipios municipio;

    @JsonFormat(pattern = "%02d")
    public Integer getCodigoDistrito() {
        return codigoDistrito;
    }
}
