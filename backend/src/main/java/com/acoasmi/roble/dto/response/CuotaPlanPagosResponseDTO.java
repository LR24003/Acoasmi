package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle individual de una cuota en la tabla de amortización")
public class CuotaPlanPagosResponseDTO {

    @Schema(description = "Número correlativo de la cuota", example = "1")
    private Integer numeroCuota;

    @Schema(description = "Fecha proyectada para el vencimiento de la cuota", example = "2026-08-30")
    private LocalDate fechaVencimientoProyectada;

    @Schema(description = "Cuota base calculada (Abono Capital + Abono Interés)", example = "709.65")
    private BigDecimal montoCuotaBase;

    @Schema(description = "Monto destinado a amortizar el capital", example = "553.40")
    private BigDecimal abonoCapital;

    @Schema(description = "Monto destinado al pago de intereses del periodo", example = "156.25")
    private BigDecimal abonoInteres;

    @Schema(description = "Monto programado por concepto de seguro de deuda", example = "15.00")
    private BigDecimal seguroProgramado;

    @Schema(description = "Monto programado por concepto de aportación", example = "10.00")
    private BigDecimal aportacionProgramada;

    @Schema(description = "Monto programado por concepto de ahorro simultáneo", example = "5.00")
    private BigDecimal ahorroProgramado;

    @Schema(description = "Monto programado por cuota de gestión", example = "2.50")
    private BigDecimal cuotaGestionProgramada;

    @Schema(description = "Monto total a pagar en la cuota (Cuota Base + Cargos adicionales)", example = "742.15")
    private BigDecimal totalCuota;

    @Schema(description = "Saldo restante del capital del préstamo tras aplicar esta cuota", example = "14446.60")
    private BigDecimal saldoCapitalRestante;

    @Schema(description = "Estado actual de la cuota (PENDIENTE, PAGADO, etc.)", example = "PENDIENTE")
    private String estadoCuota;
}