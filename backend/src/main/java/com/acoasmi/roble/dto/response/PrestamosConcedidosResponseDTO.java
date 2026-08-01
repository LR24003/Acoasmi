package com.acoasmi.roble.dto.response;

import com.acoasmi.roble.entity.PrestamosConcedidos;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para enviar la información de un préstamo concedido")
public class PrestamosConcedidosResponseDTO {

    @Schema(description = "El ID del prestamo concedido")
    private Long id;

    @Schema(description = "Número único del asociado", example = "1011", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer numeroAsociado;

    @Schema(description = "Nombre del asociado que solicita el préstamo", example = "José Mendez Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreCompletoAsociado;

    @Schema(description = "ID de la solicitud o línea de crédito asociada", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroSolicitud;

    @Schema(description = "Código o número identificador único del préstamo", example = "PREST-2026-0089", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Fecha y hora en la que se realizó el desembolso", example = "28-07-2026 10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaDesembolso;

    @Schema(description = "Monto total del préstamo otorgado", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal montoConcedido;

    @Schema(description = "Saldo actual del capital pendiente de pago", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal saldoCapitalActual;

    @Schema(description = "Porcentaje de tasa de interés anual", example = "12.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal tasaInteresAnual;

    @Schema(description = "Plazo de pago expresado en meses", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer plazoMeses;

    @Schema(description = "Frecuencia con la que se realizarán los pagos", example = "MENSUAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String frecuenciaPago;

    @Schema(description = "Estado actual del préstamo", example = "AL_DIA", requiredMode = Schema.RequiredMode.REQUIRED)
    private PrestamosConcedidos.EstadoPrestamo estadoPrestamo;

    @Schema(description = "Porcentaje de tasa de mora anual aplicable en caso de retraso", example = "3.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal tasaMoraAnual;

    @Schema(description = "Interés acumulado pendiente de pago", example = "0.00", defaultValue = "0.00")
    private BigDecimal saldoInteresPendiente;

    @Schema(description = "Mora acumulada pendiente de pago", example = "0.00", defaultValue = "0.00")
    private BigDecimal saldoMoraAcumulada;

    @Schema(description = "Monto recurrente asignado al seguro de deuda", example = "15.00", defaultValue = "0.00")
    private BigDecimal montoSeguroDeuda;

    @Schema(description = "Monto asignado a aportaciones periódicas", example = "10.00", defaultValue = "0.00")
    private BigDecimal montoAportacion;

    @Schema(description = "Monto destinado al ahorro obligatorio/simultáneo", example = "5.00", defaultValue = "0.00")
    private BigDecimal montoAhorroSimultaneo;

    @Schema(description = "Monto por cuota de administración o gestión", example = "2.50", defaultValue = "0.00")
    private BigDecimal montoCuotaGestion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(description = "Fecha proyectada del próximo pago", example = "28-08-2026")
    private LocalDate fechaProximoPago;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(description = "Fecha de registro del último pago realizado", example = "28-07-2026")
    private LocalDate fechaUltimoPago;

    @Schema(description = "Número de días de retraso actual", example = "0", defaultValue = "0")
    private Integer diasAtraso;
}
