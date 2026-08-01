package com.acoasmi.roble.dto.request;

import com.acoasmi.roble.validations.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar o actualizar una garantía crediticia")
public class CreditoGarantiasRequestDTO {

    @NotBlank(groups = OnCreate.class, message = "El tipo de garantía es obligatorio")
    @Size(max = 50, message = "El tipo de garantía no debe superar los 50 caracteres")
    @Schema(description = "Tipo de garantía (Ej: FIADOR, HIPOTECA, PRENDA_VEHICULO)", example = "FIADOR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoGarantia;

    @DecimalMin(value = "0.00", message = "El valor estimado no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El valor estimado debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto comercial o estimado del bien o respaldo", example = "15000.00")
    private BigDecimal valorEstimado;

    @NotBlank(groups = OnCreate.class, message = "La dirección de la garantía es obligatoria")
    @Size(max = 150, message = "La dirección no debe superar los 150 caracteres")
    @Schema(description = "Dirección de la propiedad o domicilio del fiador", example = "Av. España, Calle Los Almendros #12", requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccionGarantia;

    @NotBlank(groups = OnCreate.class, message = "La descripción es obligatoria")
    @Schema(description = "Descripción detallada del bien, cobertura o fiador", example = "Vehículo sedán modelo 2022 en buen estado o respaldo solidario.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    @Size(max = 150, message = "El nombre del fiador no debe superar los 150 caracteres")
    @Schema(description = "Nombre completo del fiador solidario (Si aplica)", example = "Juan Carlos Gómez")
    private String nombreFiador;

    @Size(max = 30, message = "La identificación del fiador no debe superar los 30 caracteres")
    @Schema(description = "Documento de identidad del fiador (DUI / NIT / Pasaporte)", example = "02345678-9")
    private String identificacionFiador;

    @Size(max = 20, message = "El teléfono del fiador no debe superar los 20 caracteres")
    @Schema(description = "Teléfono de contacto del fiador", example = "7788-9900")
    private String telefonoFiador;

    @DecimalMin(value = "0.00", message = "Los ingresos del fiador no pueden ser negativos")
    @Digits(integer = 10, fraction = 2, message = "Los ingresos deben tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Ingresos mensuales comprobables del fiador", example = "1250.00")
    private BigDecimal ingresosFiador;
}