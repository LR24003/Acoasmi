package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir la información de las Tasas de créditos")
public class TasaCreditosRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre del producto no debe exceder los 100 caracteres")
    @Schema(description = "Nombre de la línea del crédito", example = "Agrícola")
    private String nombreProducto;

    @NotNull(message = "La tasa de interés es obligatoria")
    @DecimalMin(value = "0.00", message = "La tasa de interés no puede ser negativa")
    @DecimalMax(value = "100.00", message = "La tasa de interés no puede ser mayor al 100%")
    @Digits(integer = 3, fraction = 2, message = "La tasa debe tener como máximo 3 enteros y 2 decimales")
    @Schema(description = "Tasa de interés anual expresada en porcentaje numérico", example = "18.00")
    private BigDecimal tasaInteresAnual;

    @NotEmpty(message = "Debe especificar al menos una frecuencia de pago permitida")
    @Schema(
            description = "Frecuencias de pago permitidas para la línea",
            example = "[\"Mensual\", \"Trimestral\", \"Semestral\", \"Anual\"]")
    private Set<String> frecuenciasPago;
}