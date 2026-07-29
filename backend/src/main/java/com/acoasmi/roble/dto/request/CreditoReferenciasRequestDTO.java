package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar o actualizar una referencia crediticia")
public class CreditoReferenciasRequestDTO {

    @NotBlank(message = "El nombre completo de la referencia es obligatorio")
    @Size(max = 150, message = "El nombre completo no debe superar los 150 caracteres")
    @Schema(description = "Nombre completo de la persona de referencia", example = "María Elena López", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreCompleto;

    @Schema(description = "Parentesco entre la referencia y el asociado", example = "Cuñado")
    private String parentesco;

    @NotBlank(message = "El teléfono de la referencia es obligatorio")
    @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
    @Schema(description = "Número telefónico de contacto de la referencia", example = "7890-1234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefono;

    @NotBlank(message = "La dirección de la referencia es obligatoria")
    @Schema(description = "Dirección de residencia o trabajo de la referencia", example = "Col. Escalón, Pasaje 3, Casa #12, San Salvador", requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccion;

    @Size(max = 30, message = "El tipo de referencia no debe superar los 30 caracteres")
    @Schema(description = "Tipo de referencia (Ej: PERSONAL, FAMILIAR, COMERCIAL)", example = "PERSONAL", defaultValue = "PERSONAL")
    @Builder.Default
    private String tipoReferencia = "PERSONAL";
}