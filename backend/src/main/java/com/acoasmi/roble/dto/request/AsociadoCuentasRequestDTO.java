package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para capturar la solicitud de apertura de una nueva cuenta pasiva")
public class AsociadoCuentasRequestDTO {

    @NotNull(message = "el numero del asociado es obligatorio")
    @Schema(description = "Numero correlativo del asociado", example = "2045")
    private Integer numeroAsociado;

    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Pattern(regexp = "^(AHORRO_VISTA|APLAZO_FIJO|APORTACIONES)$",
            message = "El tipo de cuenta debe ser exactamente: AHORRO_VISTA, APLAZO_FIJO o APORTACIONES")
    @Schema(description = "Tipo de cuenta pasiva a aperturar", example = "AHORRO_VISTA", allowableValues = {"AHORRO_VISTA", "APLAZO_FIJO", "APORTACIONES"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoCuenta;

    @NotNull(message = "El numero de cuenta es obligatorio")
    @Schema(description = "Número único de cuenta estructurado por la institución", example = "02-000842-19283")
    private String numeroCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El formato del saldo debe ser de hasta 10 enteros y 2 decimales")
    @Schema(description = "Monto con el que se abrirá la cuenta", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal saldoInicial;

    @DecimalMin(value = "0.00", message = "La tasa de interés no puede ser negativa")
    @Digits(integer = 3, fraction = 2, message = "La tasa de interés debe tener un máximo de 3 enteros y 2 decimales")
    @Schema(description = "Tasa de interés anual fija asignada a la cuenta (Obligatoria o mayor a 0 si es APLAZO_FIJO)", example = "3.50", defaultValue = "0.00")
    private BigDecimal tasaInteresAnual = BigDecimal.ZERO;

    @Min(value = 1, message = "Si es a plazo fijo, el tiempo mínimo debe ser 1 mes")
    @Schema(description = "Plazo de retención en meses (Aplica únicamente si tipoCuenta es APLAZO_FIJO)", example = "12", nullable = true)
    private Integer plazoMeses;

    @Valid
    @Schema(description = "Listado opcional de beneficiarios específicos para esta cuenta. Si se envía, la suma de porcentajes debe ser exactamente 100.00%")
    private List<AsociadosBeneficiariosRequestDTO> beneficiarios = new ArrayList<>();
}