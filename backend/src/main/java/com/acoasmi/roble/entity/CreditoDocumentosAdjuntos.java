package com.acoasmi.roble.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solicitudes_documentos_adjuntos")
@AttributeOverride(name = "id", column = @Column(name = "id_documento_adjunto"))
public class CreditoDocumentosAdjuntos extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SolicitudesCredito solicitudCredito;

    @Column(name = "tipo_documento", nullable = false, length = 100)
    private String tipoDocumento;

    @Column(name = "ruta_archivo_storage", length = 500)
    private String rutaArchivoStorage;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        if (this.fechaSubida == null) {
            this.fechaSubida = LocalDateTime.now();
        }
    }
}