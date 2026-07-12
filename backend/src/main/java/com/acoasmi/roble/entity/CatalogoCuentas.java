package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "catalogo_cuentas")
public class CatalogoCuentas extends AcoasmiEntity {

    @Column(name = "codigo_cuenta", nullable = false, unique = true, length = 30)
    private String codigoCuenta;

    @Column(name = "nombre_cuenta", nullable = false, length = 150)
    private String nombreCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;

    @Column(nullable = false)
    private Integer nivel;

    @Column(name = "naturaleza", nullable = false, length = 10)
    private String naturalezaCuenta;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta_padre")
    private CatalogoCuentas cuentaPadre;

    @OneToMany(mappedBy = "cuentaPadre", cascade = CascadeType.ALL)
    private List<CatalogoCuentas> subCuentas = new ArrayList<>();
}