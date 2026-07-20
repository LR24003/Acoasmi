package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"asociado", "cuenta"})
@Table(name = "asociados_beneficiarios")
@AttributeOverride(name = "id", column = @Column(name = "id_beneficiario"))
public class AsociadosBeneficiarios extends AcoasmiEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asociado", nullable = false)
    private Asociados asociado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta")
    private AsociadoCuentas cuenta;

    @Column(name = "nombre_beneficiario", nullable = false, length = 150)
    private String nombreBeneficiario;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "parentesco", nullable = false, length = 50)
    private String parentesco;

    @Column(name = "porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "numero_documento", length = 20)
    private String numeroDocumento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}
