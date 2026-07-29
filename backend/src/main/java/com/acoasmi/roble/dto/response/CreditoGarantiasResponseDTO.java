package com.acoasmi.roble.dto.response;

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
@Schema(description = "DTO de respuesta con los datos de una garantía")
public class CreditoGarantiasResponseDTO {

    @Schema(description = "ID único de la garantía", example = "45")
    private Long idGarantia;

    @Schema(description = "Tipo de garantía", example = "FIADOR")
    private String tipoGarantia;

    @Schema(description = "Valor estimado del bien o respaldo", example = "7500.00")
    private BigDecimal valorEstimado;

    @Schema(description = "Dirección o ubicación asociada", example = "Col. Escalón, Pasaje 3 #12")
    private String direccionGarantia;

    @Schema(description = "Descripción de la garantía", example = "Fiador solidario con empleo formal")
    private String descripcion;

    @Schema(description = "Nombre completo del fiador", example = "Carlos Eduardo Mendoza")
    private String nombreFiador;

    @Schema(description = "Documento de identidad del fiador", example = "01234567-8")
    private String identificacionFiador;

    @Schema(description = "Teléfono de contacto del fiador", example = "7766-5544")
    private String telefonoFiador;

    @Schema(description = "Ingresos mensuales comprobados del fiador", example = "1500.00")
    private BigDecimal ingresosFiador;

    @Schema(description = "Estado actual de la garantia en la base de datos", example = "true = activo")
    private Boolean estado;

}
