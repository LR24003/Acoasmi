package com.acoasmi.roble.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitudes_documentos_adjuntos")
@AttributeOverride(name = "id", column = @Column(name = "id_documento_adjunto"))
public class CreditoDocumentosAdjuntos extends AcoasmiEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_linea", nullable = false)
    private SolicitudesCredito  solicitudCredito;

    @Column(name = "tipo_documento", nullable = false, length = 100)
    private String tipoDocumento;

    @Column(name = "ruta_archivo_storage", nullable = false, length = Integer.MAX_VALUE)
    private String rutaArchivoStorage;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        this.fechaSubida = LocalDateTime.now();
    }
}
