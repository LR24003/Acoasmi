package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa el estado financiero consolidado de una sesión de caja")
public class ControlCajasResponseDTO {

    @Schema(description = "ID único de la sesión de caja", example = "1")
    private Long idSesionCaja;

    @Schema(description = "El numero de caja donde se proceso la operacion", example = "01")
    private String numeroCaja;

    @Schema(description = "Nombre completo del cajero que operó la sesión", example = "Carlos Mendoza")
    private String nombreCajero;

    @Schema(description = "Saldo inicial con el que abrió la caja", example = "100.00")
    private BigDecimal montoApertura;

    @Schema(description = "Saldo teórico que el sistema calcula según las transacciones registradas", example = "1450.00")
    private BigDecimal montoCierreTeorico;

    @Schema(description = "Efectivo físico real reportado en el arqueo o cierre", example = "1450.50")
    private BigDecimal montoCierreReal;

    @Schema(description = "Diferencia calculada (Monto Real menos Monto Teórico). Positivo es Sobrante, Negativo es Faltante.", example = "0.50")
    private BigDecimal diferencia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Fecha y hora exacta de la apertura con zona horaria", example = "15/07/2026 08:00")
    private ZonedDateTime fechaApertura;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Fecha y hora exacta del cierre de caja. Si es null, la caja sigue abierta.", example = "15/07/2026 08:00")
    private ZonedDateTime fechaCierre;

    @Schema(description = "Indica el estado operativo actual de la sesión", example = "ABIERTA")
    public String getEstadoSesion() {
        return (this.fechaCierre == null) ? "ABIERTA" : "CERRADA";
    }
}