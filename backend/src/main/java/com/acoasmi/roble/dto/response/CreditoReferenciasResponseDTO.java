package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de una referencia personal o familiar")
public class CreditoReferenciasResponseDTO {

    @Schema(description = "ID único de la referencia", example = "15")
    private Long idReferencia;

    @Schema(description = "Nombre completo de la persona de referencia", example = "María Del Carmen López")
    private String nombreCompleto;

    @Schema(description = "Parentesco o relación con el solicitante", example = "HERMANO")
    private String parentesco;

    @Schema(description = "Número telefónico de contacto", example = "7890-1234")
    private String telefono;

    @Schema(description = "Dirección de residencia o trabajo", example = "Col. Flor Blanca, Calle El Progreso #45")
    private String direccion;

    @Schema(description = "Tipo de referencia que asigna el asociado en la solicitud", example = "Personal")
    private String tipoReferencia;
}
