package com.acoasmi.roble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar o actualizar el perfil de riesgo de un asociado")
public class CumplimientoPerfilRiesgoRequestDTO {

    @Schema(description = "Nivel del riesgo que representa el asociado según el análisis", example = "BAJO")
    private String nivelRiesgo;

    @NotNull(message = "Debe especificar si el asociado es Persona Expuesta Políticamente (PEP)")
    @Schema(description = "Si es PEP = true, No es PEP = false", example = "true")
    private Boolean esPep;

    @Schema(description ="Detalla si el asociado a ocupado un cargo publico o es familia de un PEP", example = "Diputado, Alcalde")
    private String cargoOrigenPep;

    @NotNull(message = "El monto máximo mensual esperado es obligatorio")
    @Schema(description ="Monton maximo en dinero a manejar por parte del asociado", example = "600.00")
    private BigDecimal montoMaximoMensualEsperado;

    @NotBlank(message = "El origen de los fondos declarados es obligatorio")
    @Schema(description ="Origen de los fondos del asociado, según en lo que trabaja", example = "Salario por empleo en empresa privada")
    private String origenFondosDeclarado;
}
