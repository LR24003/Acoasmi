package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asociados")
@AttributeOverride(name = "id", column = @Column(name = "id_asociado"))
public class Asociados extends AcoasmiEntity {

    @Column(name = "numero_asociado", nullable = false)
    private Integer numeroAsociado;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;

    @Column(name = "nit", unique = true, length = 17)
    private String nit;

    @Column(name = "nrc", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'N/A'")
    private String nrc = "N/A";

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "edad")
    private Integer edad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_mh", referencedColumnName = "codigo_mh", nullable = false)
    private ActividadesEconomicas actividadEconomica;

    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Column(name = "correo_electronico", unique = true, length = 150, nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo_departamento")
    private Departamentos departamento;

    @ManyToOne
    @JoinColumn(name = "codigo_municipio", referencedColumnName = "codigo_municipio")
    private Municipios municipio;

    @ManyToOne
    @JoinColumn(name = "codigo_distrito", referencedColumnName = "codigo_distrito")
    private Distritos distrito;

    @Column(name = "direccion_residencia", nullable = false)
    private String direccionComplementaria;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_retiro")
    private LocalDateTime fechaRetiro;

    @Column(name = "estado", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean estado = true;

    @PrePersist
    protected void onCreate() {
        this.fechaIngreso = LocalDateTime.now();
        this.estado = true;
    }

    @PreUpdate
    protected void onUpdate() {
        if (Boolean.FALSE.equals(this.estado) && this.fechaRetiro == null) {
            this.fechaRetiro = LocalDateTime.now();
        }
    }
}