package com.acoasmi.roble.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para capturar los datos de un beneficiario en las solicitudes")
public class AsociadosBeneficiariosRequestDTO {

    @NotBlank(message = "El nombre del beneficiario no puede estar vacío")
    @Size(max = 150, message = "El nombre del beneficiario no puede exceder los 150 caracteres")
    @Schema(description = "Nombre completo del beneficiario", example = "Juan Carlos Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreBeneficiario;

    @NotBlank(message = "El telefono del beneficiario es obligatorio")
    @Schema(description = "Telefono de contacto personal del beneficiario", example = "7734-2345")
    private String telefono;

    @NotBlank(message = "El parentesco no puede estar vacío")
    @Size(max = 50, message = "El parentesco no puede exceder los 50 caracteres")
    @Schema(description = "Relación o parentesco con el titular", example = "HIJO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String parentesco;

    @NotNull(message = "El porcentaje es requerido")
    @DecimalMin(value = "0.01", message = "El porcentaje asignado debe ser mayor a 0.00%")
    @DecimalMax(value = "100.00", message = "El porcentaje asignado no puede ser mayor al 100.00%")
    @Schema(description = "Porcentaje de derecho sobre los fondos", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal porcentaje;

    @Size(max = 20, message = "El número de documento no puede exceder los 20 caracteres")
    @Schema(description = "Número de documento de identidad (Opcional si es menor de edad)", example = "01234567-8", nullable = true)
    private String numeroDocumento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de nacimiento del beneficiario", example = "03/12/1996")
    private LocalDate fechaNacimiento;
}
