package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estructurada de la tabla de amortización/plan de pagos")
public class PlanPagosResponseDTO {

    @Schema(description = "Numero correlativo del asociado", example = "1011")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado", example = "José Méndez Rivera")
    private String nombreCompletoAsociado;

    @Schema(description = "Código único del préstamo (null si es solo una simulación previa)", example = "PREST-2026-0089")
    private String numeroPrestamo;

    @Schema(description = "Monto total del crédito", example = "15000.00")
    private BigDecimal montoFinanciado;

    @Schema(description = "Tasa de interés anual aplicada (%)", example = "12.50")
    private BigDecimal tasaInteresAnual;

    @Schema(description = "Total de cuotas proyectadas", example = "24")
    private Integer totalCuotas;

    @Schema(description = "Suma total de intereses a pagar durante el plazo", example = "2041.84")
    private BigDecimal totalInteresesProyectados;

    @Schema(description = "Suma general acumulada a pagar (Capital + Intereses + Cargos adic.)", example = "17721.84")
    private BigDecimal totalGeneralProyectado;

    @Schema(description = "Detalle cuota por cuota calculado matemáticamente")
    private List<CuotaPlanPagosResponseDTO> cuotas;
}
