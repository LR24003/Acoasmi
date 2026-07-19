package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cumplimiento_perfil_riesgo")
@AttributeOverride(name = "id", column = @Column(name = "id_perfil_cumplimiento"))
public class CumplimientoPerfilRiesgo extends AcoasmiEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asociado", nullable = false, unique = true)
    private Asociados asociado;

    @Column(name = "nivel_riesgo", nullable = false, length = 20)
    private String nivelRiesgo = "BAJO";

    @Column(name = "es_pep", nullable = false)
    private Boolean esPep = false;

    @Column(name = "cargo_origen_pep", length = 150)
    private String cargoOrigenPep;

    @Column(name = "monto_maximo_mensual_esperado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoMaximoMensualEsperado;

    @Column(name = "origen_fondos_declarado", nullable = false, columnDefinition = "TEXT")
    private String origenFondosDeclarado;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }
}
