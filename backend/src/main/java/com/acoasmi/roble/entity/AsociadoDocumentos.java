package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asociado_documentos")
@AttributeOverride(name = "id", column = @Column(name = "id_documento"))
public class AsociadoDocumentos extends AcoasmiEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asociado", nullable = false)
    private Asociados asociado;

    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "url_archivo", nullable = false, columnDefinition = "text")
    private String urlArchivo;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        if (this.fechaSubida == null) {
            this.fechaSubida = LocalDateTime.now();
        }
    }
}
