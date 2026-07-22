package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Pattern(regexp = "^(AHORRO_A_LA_VISTA|AHORRO_A_PLAZO_FIJO|APORTACIONES)$",
            message = "El tipo de cuenta debe ser exactamente: AHORRO_A_LA_VISTA, AHORRO_A_PLAZO_FIJO o APORTACIONES")
    @Schema(description = "Tipo de cuenta pasiva a aperturar", example = "AHORRO_A_LA_VISTA", allowableValues = {"AHORRO_A_LA_VISTA", "AHORRO_A_PLAZO_FIJO", "APORTACIONES"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoCuenta;

    @NotNull(message = "El numero de cuenta es obligatorio")
    @Schema(description = "Número único de cuenta estructurado por la institución", example = "1011-0000-3")
    private String numeroCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El formato del saldo debe ser de hasta 10 enteros y 2 decimales")
    @Schema(description = "Monto con el que se abrirá la cuenta", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal saldoActual;

    @DecimalMin(value = "0.00", message = "La tasa de interés no puede ser negativa")
    @Digits(integer = 3, fraction = 2, message = "La tasa de interés debe tener un máximo de 3 enteros y 2 decimales")
    @Schema(description = "Tasa de interés anual fija asignada a la cuenta (Obligatoria o mayor a 0 si es APLAZO_FIJO)", example = "3.50", defaultValue = "0.00")
    private BigDecimal tasaInteresAnual = BigDecimal.ZERO;

    @Min(value = 30, message = "Si es a plazo fijo, el tiempo mínimo debe ser de 30 días")
    @Schema(
            description = "Plazo de retención en días. Requiere un mínimo de 30 días y aplica Exclusivamente si tipoCuenta es 'AHORRO_A_PLAZO_FIJO'. Para 'AHORRO A LA VISTA' se asigna automáticamente 360 y para 'APORTACIONES' queda vacío (null).",
            example = "30",
            allowableValues = {"30", "60", "90", "180", "360"},
            nullable = true
    )
    private String plazoDias;

    @NotBlank(message = "El usuario es obligatorio.")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "El usuario solo puede contener letras, números, puntos, guiones y guiones bajos.")
    @Schema(
            description = "Nombre de usuario (username) del operador/cajero que realiza el registro",
            example = "Jose Perez",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50
    )
    private String usuario;

    @Size(max = 50, message = "El tipo de ahorro no debe exceder los 50 caracteres")
    @Schema(description = "Subtipo o modalidad específica de ahorro", example = "AHORRO PROGRAMADO", nullable = true)
    private String tipoAhorro;

    @PastOrPresent(message = "La fecha de última capitalización no puede ser una fecha futura")
    @Schema(description = "Fecha del último corte o pago de intereses ejecutado", example = "31-12-2025", nullable = true)
    private LocalDate fechaUltimaCapitalizacion;

    @NotNull(message = "El monto de apertura es obligatorio")
    @DecimalMin(value = "0.00", message = "El monto de apertura no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El monto de apertura debe tener como máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto asignado al aperturar la cuenta", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal montoApertura;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El saldo inicial debe tener como máximo 10 enteros y 2 decimales")
    @Schema(description = "Saldo inicial asignado para aperturas o inicios de periodo", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal saldoInicial;

    @Valid
    @Schema(description = "Listado opcional de beneficiarios específicos para esta cuenta. Si se envía, la suma de porcentajes debe ser exactamente 100.00%")
    private List<AsociadosBeneficiariosRequestDTO> beneficiarios = new ArrayList<>();
}