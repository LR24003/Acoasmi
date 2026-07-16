package com.acoasmi.roble.entity;

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

    @Column(name = "codigo_municipio", nullable = false)
    private String codigoMunicipio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento", nullable = false)
    private Departamentos departamento;
}
