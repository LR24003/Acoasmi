package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que envia la información de los movimientos realizados por un asociado")
public class AsociadoMovimientosResponseDTO {

    @Schema(description = "Numero de la caja donde se realizo el movimiento", example = "01")
    private String numeroCaja;

    @Schema(description = "Nombre de usuario (username) del operador/cajero que realiza el registro")
    private String usuarioCajero;

    @Schema(
            description = "Identificador único correlativo del movimiento generado en la base de datos.",
            example = "7412"
    )
    private Long idMovimiento;

    @Schema(description = "numero correlativo del comprobante", example = "01-210938")
    private String numeroComprobante;

    @Schema(
            description = "Nombre completo del asociado titular de la cuenta afectada.",
            example = "José Méndez Rivera"
    )
    private String nombreCompletoAsociado;

    @Schema(
            description = "Número institucional de la cuenta donde se aplicó la transacción.",
            example = "1011-1001-3"
    )
    private String numeroCuenta;

    @Schema(
            description = "Categoría o tipo de cuenta pasiva afectada.",
            example = "AHORRO_A_LA_VISTA"
    )
    private String tipoCuenta;

    @Schema(
            description = "Tipo de operación ejecutada en mayúsculas.",
            example = "DEPOSITO A CUENTA O PAGO DE CUOTA PRESTAMO"
    )
    private String tipoMovimiento;

    @Schema(
            description = "Monto exacto que ingresó o retiró de la cuenta.",
            example = "500.00"
    )
    private BigDecimal monto;

    @Schema(
            description = "Saldo final de la cuenta calculado inmediatamente después de aplicar la transacción por el trigger de la base de datos.",
            example = "2000.00"
    )
    private BigDecimal saldoResultante;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Schema(
            description = "Fecha y hora exacta de cuándo se consolidó el movimiento en el servidor.",
            example = "20/07/2026 14:30:00"
    )
    private LocalDateTime fechaMovimiento;

    @Schema(
            description = "Breve concepto o justificación comercial del movimiento bancario.",
            example = "Depósito inicial en efectivo en ventanilla central."
    )
    private String descripcionMovimiento;

    private BigDecimal cambioTasa;

}
