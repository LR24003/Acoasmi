package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para recibir la metadata de los documentos de un Asociado")
public class AsociadoDocumentosRequestDTO {

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 50, message = "El tipo de documento no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El tipo de documento solo puede contener letras, números y guiones bajos")
    @Schema(
            description = "Tipo de documento que se está cargando al expediente",
            example = "DUI",
            allowableValues = {"DUI", "NIT", "CONSTANCIA_SUELDO", "RECIBO_LUZ", "OTRO"}
    )
    private String tipoDocumento;
}