package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asociado_cuentas")
@AttributeOverride(name = "id", column = @Column(name = "id_cuenta"))
@ToString(exclude = {"asociado", "beneficiarios"})
public class AsociadoCuentas extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asociado", nullable = false)
    private Asociados asociado;

    @Column(name = "tipo_cuenta", nullable = false, length = 50)
    private String tipoCuenta;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 30)
    private String numeroCuenta;

    @Column(name = "saldo_actual", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Column(name = "tasa_interes_anual", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual = BigDecimal.ZERO;

    @Column(name = "estado_cuenta", nullable = false, length = 20)
    private String estadoCuenta = "ACTIVA";

    @Column(name = "fecha_apertura", nullable = false, updatable = false)
    private ZonedDateTime fechaApertura;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsociadosBeneficiarios> beneficiarios = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.fechaApertura == null) {
            this.fechaApertura = ZonedDateTime.now();
        }
    }

    public void addBeneficiario(AsociadosBeneficiarios beneficiario) {
        beneficiarios.add(beneficiario);
        beneficiario.setCuenta(this);
    }

    public void removeBeneficiario(AsociadosBeneficiarios beneficiario) {
        beneficiarios.remove(beneficiario);
        beneficiario.setCuenta(null);
    }
}