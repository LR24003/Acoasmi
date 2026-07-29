package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de una línea de crédito")
public class TasaCreditosResponseDTO {

    @Schema(description = "ID único de la línea de crédito", example = "1")
    private Long id;

    @Schema(description = "Nombre de la línea del crédito", example = "Agrícola")
    private String nombreProducto;

    @Schema(description = "Tasa de interés anual expresada en porcentaje numérico", example = "18.00")
    private BigDecimal tasaInteresAnual;

    @Schema(description = "Frecuencias de pago disponibles")
    private Set<String> frecuenciasPago;

    @Schema(description = "Estado de la línea de crédito (true = Activo, false = Inactivo)", example = "true")
    private Boolean estado;
}