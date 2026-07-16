package com.acoasmi.roble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para enviar los datos de una actividad económica")
public class ActividadesEconomicasResponseDTO {

    @Schema(description = "ID de la actividad económica en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Codigo de la actividad económica según MH", example = "10711")
    private String codigoMh;

    @Schema(description = "Descripcion de la actividad económica", example = "Elaboración de tortillas")
    private String descripcion;

    @Schema(description = "Estado de la actividad económica (true = Activa, false = Inactiva)", example = "true")
    private Boolean estado;
}
