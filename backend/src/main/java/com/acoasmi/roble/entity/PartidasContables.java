package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partidas_contables")
@AttributeOverride(name = "id", column = @Column(name = "id_partida"))
public class PartidasContables extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo")
    private PeriodosContables periodoContable;

    @Column(name = "numero_partida", nullable = false)
    private Integer numeroPartida;

    @Column(name = "fecha_partida", nullable = false)
    private LocalDate fechaPartida;

    @Column(name = "concepto", nullable = false)
    private String concepto;

    @ColumnDefault("'POSTEADA'")
    @Column(name = "estado_partida", nullable = false, length = 20)
    private String estadoPartida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuarios usuario;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }


}
