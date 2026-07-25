package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class DetalleMovimientoEstadoCuentaDTO {

    @Schema(description = "numero de comprobante del movimiento realizado", example = "01-000006")
    private String numeroComprobante;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(description = "Fecha en la que se aplico el movimiento", example = "22-07-2026")
    private LocalDate fechaAplicacion;

    @Schema(description = "Muestra todos los registros que han sido de tipo deposito")
    private BigDecimal deposito;

    @Schema(description = "Muestra todos los registros que han sido de tipo retiro")
    private BigDecimal retiro;

    @Schema(description = "Muestra todos los registros que han sido de tipo intereses")
    private BigDecimal intereses;

    @Schema(description = "Muestra todos los registros en donde ha existido un cambio de tasa")
    private BigDecimal cambioTasa;

    @Schema(description = "Muestra el saldo total a la fecha de la cuenta del asociado")
    private BigDecimal saldo;
}
