package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar la apertura de una nueva sesión de caja")
public class CajaAperturaRequestDTO {

    @NotBlank(message = "El ID del usuario cajero es obligatorio")
    @Schema(description = "Nombre del cajero que operará la caja", example = "Alfredo López")
    private String usuarioCajero;

    @NotNull(message = "El monto de apertura no puede ser nulo")
    @PositiveOrZero(message = "El monto de apertura debe ser mayor o igual a cero")
    @Schema(description = "Monto de efectivo inicial asignado a la gaveta de la caja", example = "100.00")
    private BigDecimal montoApertura;
}