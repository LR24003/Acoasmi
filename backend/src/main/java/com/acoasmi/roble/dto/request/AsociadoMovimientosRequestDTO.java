package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de entrada para la validación y registro de depósitos, retiros y pagos.")
public class AsociadoMovimientosRequestDTO {

    @NotBlank(message = "El usuario es obligatorio.")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres.")
    @Schema(
            description = "Nombre de usuario (username) del operador/cajero que realiza el registro",
            example = "Jose Perez",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50
    )
    private String usuarioCajero;

    @Schema(
            description = "Número único institucional de la cuenta de ahorros o aportaciones afectada.",
            example = "1011-1001-1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El número de cuenta es obligatorio.")
    @Size(max = 50, message = "El número de cuenta no puede exceder los 50 caracteres.")
    private String numeroCuenta;

    @Schema(
            description = "Operación monetaria a realizar sobre la cuenta pasiva.",
            example = "DEPOSITO",
            allowableValues = {"DEPOSITO", "RETIRO", "PAGO_PRESTAMO"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El tipo de movimiento es obligatorio.")
    @Pattern(
            regexp = "^(?i)(DEPOSITO|RETIRO|PAGO_PRESTAMO)$",
            message = "El tipo de movimiento debe ser estrictamente 'DEPOSITO', 'RETIRO' o 'PAGO_PRESTAMO'." // 👈 Corregido el texto del mensaje
    )
    private String tipoMovimiento;

    @Schema(
            description = "Importe monetario exacto de la transacción (debe ser mayor a 0.00).",
            example = "150.50",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El monto del movimiento es obligatorio.")
    @DecimalMin(value = "0.01", message = "El monto del movimiento debe ser un valor positivo mayor a 0.00.")
    private BigDecimal monto;

    @Schema(
            description = "Detalle conceptual o justificación comercial del movimiento bancario.",
            example = "Depósito inicial en efectivo en ventanilla central.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La descripción del movimiento es obligatoria.")
    @Size(min = 5, max = 500, message = "La descripción debe contener entre 5 y 500 caracteres.")
    private String descripcionMovimiento;

    @Schema(
            description = "ID del préstamo concedido si el movimiento corresponde al pago de una cuota de crédito (Requerido si tipoMovimiento es PAGO_PRESTAMO).",
            example = "45",
            nullable = true
    )
    private Long idPrestamo;

    @Schema(
            description = "ID de la factura de referencia comercial vinculada al cobro o dispersión.",
            example = "1024",
            nullable = true
    )
    private Long idFacturaReferencia;
}