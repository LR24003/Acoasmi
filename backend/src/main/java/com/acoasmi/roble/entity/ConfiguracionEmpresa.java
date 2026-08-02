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
@Table(name = "configuracion_empresa")
@AttributeOverride(name = "id", column = @Column(name = "id_configuracion"))
public class ConfiguracionEmpresa extends AcoasmiEntity{

    @Column(name = "nombre_financiera", nullable = false, length = 150)
    private String nombreFinanciera;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "nit", nullable = false, length = 17)
    private String nit;

    @Column(name = "nrc", nullable = false, length = 10)
    private String nrc;

    @Column(name = "actividad_economica", nullable = false)
    private String actividadEconomica;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "correo_electronico", nullable = false, length = 150)
    private String correoElectronico;


}
