package com.acoasmi.roble.entity;

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
@Table(name = "actividades_economicas")
@AttributeOverride(name = "id", column = @Column(name = "id_actividad"))
public class ActividadesEconomicas extends AcoasmiEntity{

    @Column(name = "codigo_mh", unique = true, nullable = false)
    private String codigoMh;

    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

}
