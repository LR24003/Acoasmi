package com.acoasmi.roble.dto.request;

import com.acoasmi.roble.enums.TipoDeduccion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la asignación de deducciones individuales en el desembolso de crédito")
public class DesembolsoDeduccionesRequestDTO {

    @NotNull(message = "El tipo de deducción es obligatorio")
    @Schema(description = "Tipo o catálogo de la deducción aplicada", example = "COMISION_APERTURA")
    private TipoDeduccion tipoDeduccion;

    @NotNull(message = "El monto de la deducción es obligatorio")
    @Positive(message = "El monto de la deducción debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El monto no puede exceder 10 enteros y 2 decimales")
    @Schema(description = "Monto a deducir en la transacción", example = "25.00")
    private BigDecimal monto;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Schema(description = "Descripción o justificación opcional de la deducción", example = "Cobro por papelería y trámite administrativo")
    private String descripcion;
}
