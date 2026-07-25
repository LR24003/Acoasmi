package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para vincular una referencia a la solicitud de crédito con su parentesco")
public class SolicitudReferenciaRelacionRequestDTO {
    
    @Size(max = 50, message = "El parentesco o relación no debe superar los 50 caracteres")
    @Schema(description = "Parentesco o vínculo respecto al solicitante en ESTA solicitud", example = "Hermano/a", requiredMode = Schema.RequiredMode.REQUIRED)
    private String parentescoRelacion;

    @Valid
    @Schema(description = "Datos completos de la referencia (Obligatorio solo si es una referencia NUEVA)")
    private CreditoReferenciasRequestDTO datosReferencia;
}
