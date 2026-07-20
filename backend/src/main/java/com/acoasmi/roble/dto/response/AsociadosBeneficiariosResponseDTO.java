package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que envia la información de un beneficiario registrado")
public class AsociadosBeneficiariosResponseDTO {

    @Schema(description = "ID único del beneficiario generado por el sistema", example = "15")
    private Long id;

    @Schema(description = "Numero de cuenta vinculado al beneficiario", example = "1011-1850-1")
    private String numeroCuenta;

    @Schema(description = "Nombre completo del beneficiario", example = "Juan Carlos Pérez")
    private String nombreBeneficiario;

    @Schema(description = "Telefono de contacto personal del beneficiario", example = "7734-2345")
    private String telefono;

    @Schema(description = "Relación o parentesco con el titular", example = "HIJO")
    private String parentesco;

    @Schema(description = "Porcentaje asignado asignado al beneficiario", example = "50.00")
    private BigDecimal porcentaje;

    @Schema(description = "Número de documento de identidad", example = "01234567-8")
    private String numeroDocumento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de nacimiento del beneficiario", example = "23-08-2006")
    private LocalDate fechaNacimiento;
}