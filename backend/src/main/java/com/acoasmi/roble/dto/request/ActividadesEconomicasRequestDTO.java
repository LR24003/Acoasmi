package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir los datos de las actividades economicas")
public class ActividadesEconomicasRequestDTO {

    @NotBlank(message = "El codigo de la actividad es obligatorio")
    @Column(name = "codigo_mh", unique = true, nullable = false, length = 10)
    private String codigoMh;

    @NotNull(message = "La descripcion de la actividad es obligatoria")
    @Schema(description = "Descripcion de la actividad económica", example = "Elaboración de tortillas")
    private String descripcion;
}
