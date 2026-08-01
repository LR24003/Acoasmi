package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para solicitar la generación y persistencia del plan de pagos oficial de un préstamo aprobado")
public class GenerarPlanPagosRequestDTO {

    @NotBlank(message = "El número de préstamo es obligatorio")
    @Size(max = 50, message = "El número de préstamo no debe exceder 50 caracteres")
    @Schema(
            description = "Código único del préstamo otorgado (el servicio leerá internamente el monto, tasa, fecha desembolso y plazo)",
            example = "PREST-2026-0089",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String numeroPrestamo;
}