package com.acoasmi.roble.dto.request;

import com.acoasmi.roble.enums.EstadoPrestamo;
import com.acoasmi.roble.enums.FrecuenciaPago;
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
@Schema(description = "DTO de transferencia para la creación/registro de un préstamo concedido")
public class PrestamosConcedidosRequestDTO {

    @NotNull(message = "El numero correlativo del asociado es obligatorio")
    @Schema(description = "Numero correlativo del asociado que solicita el préstamo", example = "1011", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numeroAsociado;

    @NotBlank(message = "El numero de la solicitud/línea de crédito es obligatorio")
    @Schema(description = "Numero de la solicitud o línea de crédito asociada", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroSolicitud;

    @NotBlank(message = "El número de préstamo es obligatorio")
    @Size(max = 20, message = "El número de préstamo no debe exceder 20 caracteres")
    @Schema(description = "Código o número identificador único del préstamo", example = "PREST-2026-0089", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroPrestamo;

    @NotNull(message = "El monto concedido es obligatorio")
    @Positive(message = "El monto concedido debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El formato del monto debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto total del préstamo otorgado", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal montoConcedido;

    @NotNull(message = "El saldo capital actual es obligatorio")
    @PositiveOrZero(message = "El saldo capital no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Saldo actual del capital pendiente de pago", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal saldoCapitalActual;

    @NotNull(message = "La tasa de interés anual es obligatoria")
    @PositiveOrZero(message = "La tasa de interés no puede ser negativa")
    @Digits(integer = 3, fraction = 2)
    @Schema(description = "Porcentaje de tasa de interés anual", example = "12.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal tasaInteresAnual;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es de 1 mes")
    @Max(value = 360, message = "El plazo máximo no debe exceder 360 meses")
    @Schema(description = "Plazo de pago expresado en meses", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer plazoMeses;

    @NotNull(message = "La frecuencia de pago es obligatoria")
    @Schema(description = "Frecuencia con la que se realizarán los pagos", example = "MENSUAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private FrecuenciaPago frecuenciaPago;

    @NotNull(message = "El estado del préstamo es obligatorio")
    @Schema(description = "Estado actual del préstamo", example = "AL_DIA", requiredMode = Schema.RequiredMode.REQUIRED)
    private EstadoPrestamo estadoPrestamo;

    @Builder.Default
    @PositiveOrZero(message = "La tasa de mora no puede ser negativa")
    @Digits(integer = 3, fraction = 2, message = "La tasa de mora debe tener máximo 3 enteros y 2 decimales")
    @Schema(
            description = "Porcentaje de tasa de mora anual aplicable en caso de retraso",
            example = "36.00",
            defaultValue = "36.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private BigDecimal tasaMoraAnual = new BigDecimal("36.00");

    @Builder.Default
    @NotNull(message = "El saldo de interés pendiente es obligatorio")
    @PositiveOrZero(message = "El saldo de interés no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Interés acumulado pendiente de pago", example = "0.00", defaultValue = "0.00")
    private BigDecimal saldoInteresPendiente = BigDecimal.ZERO;

    @Builder.Default
    @NotNull(message = "El saldo de mora acumulada es obligatorio")
    @PositiveOrZero(message = "El saldo de mora no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Mora acumulada pendiente de pago", example = "0.00", defaultValue = "0.00")
    private BigDecimal saldoMoraAcumulada = BigDecimal.ZERO;

    @Builder.Default
    @NotNull(message = "El monto de seguro de deuda es obligatorio")
    @PositiveOrZero(message = "El monto de seguro no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto recurrente asignado al seguro de deuda", example = "15.00", defaultValue = "0.00")
    private BigDecimal montoSeguroDeuda = BigDecimal.ZERO;

    @Builder.Default
    @NotNull(message = "El monto de aportación es obligatorio")
    @PositiveOrZero(message = "El monto de aportación no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto asignado a aportaciones periódicas", example = "10.00", defaultValue = "0.00")
    private BigDecimal montoAportacion = BigDecimal.ZERO;

    @Builder.Default
    @NotNull(message = "El monto de ahorro simultáneo es obligatorio")
    @PositiveOrZero(message = "El monto de ahorro no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto destinado al ahorro obligatorio/simultáneo", example = "5.00", defaultValue = "0.00")
    private BigDecimal montoAhorroSimultaneo = BigDecimal.ZERO;

    @Builder.Default
    @NotNull(message = "El monto de cuota de gestión es obligatorio")
    @PositiveOrZero(message = "El monto de gestión no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Monto por cuota de administración o gestión", example = "2.50", defaultValue = "0.00")
    private BigDecimal montoCuotaGestion = BigDecimal.ZERO;

    @Builder.Default
    @NotNull(message = "Los días de atraso son obligatorios")
    @Min(value = 0, message = "Los días de atraso no pueden ser negativos")
    @Schema(description = "Número de días de retraso actual", example = "0", defaultValue = "0")
    private Integer diasAtraso = 0;
}
