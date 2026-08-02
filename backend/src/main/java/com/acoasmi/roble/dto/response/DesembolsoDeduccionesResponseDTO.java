package com.acoasmi.roble.dto.response;

import com.acoasmi.roble.enums.TipoDeduccion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con el detalle de cada deducción aplicada al desembolso")
public class DesembolsoDeduccionesResponseDTO {

    @Schema(description = "Tipo de deducción aplicada", example = "COMISION_OTORGAMIENTO")
    private TipoDeduccion tipoDeduccion;

    @Schema(description = "Monto descontado", example = "25.00")
    private BigDecimal monto;

    @Schema(description = "Descripción o detalle de la deducción", example = "Cobro por papelería y trámite administrativo")
    private String descripcion;
}