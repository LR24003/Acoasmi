package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con la información del documento adjunto a la solicitud de crédito")
public class CreditoDocumentosAdjuntosResponseDTO {

    @Schema(description = "ID único del documento adjunto", example = "50")
    private Long idDocumentoAdjunto;

    @Schema(description = "Tipo de documento cargado", example = "DUI")
    private String tipoDocumento;

    @Schema(description = "Ruta o URI de almacenamiento del archivo", example = "uploads/solicitudes/50/dui.pdf")
    private String rutaArchivoStorage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Fecha y hora en que se subió el archivo", example = "12-08-2026 09:34:12")
    private LocalDateTime fechaSubida;
}
