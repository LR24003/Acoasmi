package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que envia la información de los documentos en digital de cada asociado")
public class AsociadoDocumentosResponseDTO {

    @Schema(description = "ID interno del documento en la base de datos", example = "1")
    private Long idDocumento;

    @Schema(description = "Numero correlativo del asociado en la base de datos", example = "1850")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado en la base de datos", example = "José Alfredo López Rivera")
    private String nombreCompletoAsociado;

    @Schema(description = "Tipo de documento subido", example = "DUI")
    private String tipoDocumento;

    @Schema(description = "Nombre con el cual se almacena el documento a subido", example = "Dui_Alfredo_Lopez")
    private String nombreArchivo;

    @Schema(description = "Url del documento subido", example = "http://acoasmi/AsociadoDocumentos/Dui_Alfredo_Lopez.pdf")
    private String urlArchivo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Fecha en la que se subio del documento al sistema", example = "18/07/2026 08:45")
    private LocalDateTime fechaSubida;

    @Schema(description = "Estado actual del documento en la base de datos", example = "true")
    private Boolean estado;
}