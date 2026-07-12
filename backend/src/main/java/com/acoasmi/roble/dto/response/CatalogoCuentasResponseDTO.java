package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para retornar los datos de una cuenta contable con su árbol jerárquico")
public class CatalogoCuentasResponseDTO {

    @Schema(description = "ID de la cuenta contable", example = "12")
    private Long id;

    @Schema(description = "Código estructurado de la cuenta", example = "1011-01-01")
    private String codigoCuenta;

    @Schema(description = "Nombre descriptivo de la cuenta", example = "Caja General")
    private String nombreCuenta;

    @Schema(description = "Tipo de cuenta", example = "ACTIVO")
    private String tipoCuenta;

    @Schema(description = "Nivel de profundidad jerárquica", example = "4")
    private Integer nivel;

    @Schema(description = "Naturaleza contable de la cuenta", example = "DEUDORA")
    private String naturalezaCuenta;

    @Schema(description = "Indica si la cuenta está disponible para operaciones", example = "true")
    private Boolean activa;

    @Schema(description = "Código de la cuenta padre de la cual depende", example = "1011-01")
    private String codigoCuentaPadre;

    @Schema(description = "ID de la cuenta padre", example = "5")
    private Long idCuentaPadre;

    @Schema(description = "Listado de subcuentas que dependen directamente de esta cuenta")
    private List<CatalogoCuentasResponseDTO> subCuentas;
}
