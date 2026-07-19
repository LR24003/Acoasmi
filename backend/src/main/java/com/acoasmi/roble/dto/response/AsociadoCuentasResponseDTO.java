package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AsociadoCuentasResponseDTO", description = "Modelo que representa los datos devueltos tras consultar o crear una cuenta pasiva")
public class AsociadoCuentasResponseDTO {

    @Schema(description = "ID único de la cuenta pasiva generado por el sistema", example = "1")
    private Long idCuenta;

    @Schema(description = "Número único de cuenta estructurado por la institución", example = "02-000842-19283")
    private String numeroCuenta;

    @Schema(description = "Código o número único identificador del asociado", example = "2045")
    private String numeroAsociado;

    @Schema(description = "Nombre completo del asociado titular de la cuenta", example = "María Mercedes Quintanilla")
    private String nombreCompletoAsociado;

    @Schema(description = "Tipo de cuenta pasiva", example = "AHORRO_VISTA")
    private String tipoCuenta;

    @Schema(description = "Tasa de interés anualizada que devenga la cuenta", example = "2.50")
    private BigDecimal tasaInteresAnual;

    @Schema(description = "Saldo disponible actual en la cuenta", example = "1250.50")
    private BigDecimal saldoActual;

    @Schema(description = "Estado operativo actual de la cuenta", example = "ACTIVA")
    private String estadoCuenta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Fecha y hora exacta en la que se realizó la apertura", example = "19-07-2026 08:45")
    private ZonedDateTime fechaApertura;

    @Schema(description = "Listado de beneficiarios asignados a esta cuenta pasiva")
    private List<AsociadosBeneficiariosResponseDTO> beneficiarios;

    @Schema(description = "Estado actual de la cuenta en la base de datos", example = "true")
    private Boolean estado;

   
}
