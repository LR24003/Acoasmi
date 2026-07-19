package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "DTO para procesar el cuadre físico y cierre de la sesión de caja")
public class CajaCierreRequestDTO {

    @NotNull(message = "El monto de cierre real (dinero físico) es obligatorio")
    @PositiveOrZero(message = "El monto de cierre real debe ser mayor o igual a cero")
    @Schema(description = "Dinero en efectivo total contado físicamente por el cajero en su caja", example = "1450.50")
    private BigDecimal montoCierreReal;
}