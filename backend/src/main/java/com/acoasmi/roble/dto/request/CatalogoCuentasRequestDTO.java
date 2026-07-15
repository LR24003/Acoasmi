package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación o modificación de una cuenta contable")
public class CatalogoCuentasRequestDTO {

    @NotBlank(message = "El código de la cuenta no puede estar vacío")
    @Size(max = 30, message = "El código no debe superar los 30 caracteres")
    @Schema(description = "Código estructurado de la cuenta", example = "1011-01-01")
    private String codigoCuenta;

    @NotBlank(message = "El nombre de la cuenta no puede estar vacío")
    @Size(max = 150, message = "El nombre no debe superar los 150 caracteres")
    @Schema(description = "Nombre descriptivo de la cuenta", example = "Caja General")
    private String nombreCuenta;

    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Size(max = 20, message = "El tipo de cuenta no debe superar los 20 caracteres")
    @Schema(description = "Clasificación de la cuenta (ACTIVO, PASIVO, PATRIMONIO)", example = "ACTIVO")
    private String tipoCuenta;

    @NotNull(message = "El nivel de la cuenta es obligatorio")
    @Min(value = 1, message = "El nivel mínimo debe ser 1")
    @Schema(description = "Nivel jerárquico de la cuenta en el catálogo", example = "4")
    private Integer nivel;

    @NotBlank(message = "La naturaleza de la cuenta es obligatoria")
    @Size(max = 10, message = "La naturaleza no debe superar los 10 caracteres")
    @Schema(description = "Naturaleza contable (DEUDORA o ACREEDORA)", example = "DEUDORA")
    private String naturalezaCuenta;

    @Schema(description = "ID de la cuenta de la cual depende. Nulo si es una cuenta de nivel 1", example = "5")
    private Long idCuentaPadre;

}
