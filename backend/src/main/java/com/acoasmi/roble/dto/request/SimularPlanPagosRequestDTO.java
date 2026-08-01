package com.acoasmi.roble.dto.request;

import com.acoasmi.roble.entity.PrestamosConcedidos.FrecuenciaPago;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para simular la tabla de amortización antes de aprobar el crédito")
public class SimularPlanPagosRequestDTO {

    @Schema(description = "Número correlativo del asociado", example = "1011")
    private Integer numeroAsociado;

    @NotNull(message = "El monto a simular es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El monto concedido debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto a financiar", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal montoConcedido;

    @NotNull(message = "La tasa de interés anual es obligatoria")
    @PositiveOrZero(message = "La tasa de interés no puede ser negativa")
    @Digits(integer = 3, fraction = 2, message = "La tasa de interés debe tener máximo 3 enteros y 2 decimales")
    @Schema(description = "Tasa de interés anual (%)", example = "12.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal tasaInteresAnual;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es 1 mes")
    @Max(value = 360, message = "El plazo máximo es 360 meses")
    @Schema(description = "Plazo total en meses", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer plazoMeses;

    @NotNull(message = "La frecuencia de pago es obligatoria")
    @Schema(description = "Frecuencia de las cuotas", example = "MENSUAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private FrecuenciaPago frecuenciaPago;

    @Builder.Default
    @PositiveOrZero(message = "El seguro de deuda no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto opcional de seguro por cuota", example = "15.00", defaultValue = "0.00")
    private BigDecimal montoSeguroDeuda = BigDecimal.ZERO;

    @Builder.Default
    @PositiveOrZero(message = "La aportación no puede ser negativa")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto opcional de aportación por cuota", example = "10.00", defaultValue = "0.00")
    private BigDecimal montoAportacion = BigDecimal.ZERO;

    @Builder.Default
    @PositiveOrZero(message = "El ahorro simultáneo no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto opcional de ahorro simultáneo por cuota", example = "5.00", defaultValue = "0.00")
    private BigDecimal montoAhorroSimultaneo = BigDecimal.ZERO;

    @Builder.Default
    @PositiveOrZero(message = "La cuota de gestión no puede ser negativa")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto opcional de cuota de gestión", example = "2.50", defaultValue = "0.00")
    private BigDecimal montoCuotaGestion = BigDecimal.ZERO;
}