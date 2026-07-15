package com.acoasmi.roble.entity;


import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permisos")
@AttributeOverride(name = "id", column = @Column(name = "id_permiso"))
public class Permisos extends AcoasmiEntity {

    @Column(name = "codigo_permiso", nullable = false, unique = true, length = 50)
    private String codigoPermiso;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;
}
