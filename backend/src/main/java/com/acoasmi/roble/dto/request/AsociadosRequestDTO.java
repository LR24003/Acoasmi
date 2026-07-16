package com.acoasmi.roble.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recibir y validar los datos de creación o actualización de un Asociado")
public class AsociadosRequestDTO {

    @NotNull(message = "El número de asociado es obligatorio")
    @Schema(description = "Número correlativo único asignado al asociado", example = "1850")
    private Integer numeroAsociado;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres")
    @Schema(description = "Nombres del asociado", example = "José Alfredo")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres")
    @Schema(description = "Apellidos del asociado", example = "López Rivera")
    private String apellidos;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 30, message = "El tipo de documento no puede exceder los 30 caracteres")
    @Schema(description = "Tipo de identificación del asociado", example = "DUI")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30, message = "El número de documento no puede exceder los 30 caracteres")
    @Schema(description = "Número único del documento de identidad", example = "01234567-8")
    private String numeroDocumento;

    @Size(max = 20, message = "El NIT no puede exceder los 20 caracteres")
    @Schema(description = "Número de Identificación Tributaria", example = "0614-150795-101-4")
    private String nit;

    @Size(max = 10, message = "El NRC no puede exceder los 10 caracteres")
    @Schema(description = "Número de Registro de Contribuyente. Por defecto es N/A si no es comerciante.", example = "123456-7")
    private String nrc = "N/A";

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Schema(description = "Fecha de nacimiento del asociado", example = "15-07-1995")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La actividad económica es obligatoria")
    @Schema(description = "Nombre del catálogo maestro de Actividades Económicas", example = "Elaboración de tortillas")
    private String actividadEconomica;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    @Schema(description = "Teléfono de contacto del asociado", example = "2255-0000")
    private String telefono;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
    @Schema(description = "Correo electrónico del asociado", example = "josepalfred7@gmail.com")
    private String email;

    @NotBlank(message = "El departamento es obligatorio")
    @Schema(description = "Nombre del catálogo maestro de Departamentos", example = "San Vicente")
    private String nombreDepartamento;

    @NotBlank(message = "El municipio es obligatorio")
    @Schema(description = "Nombre del catálogo maestro de Municipios", example = "San Vicente Sur")
    private String nombreMunicipio;

    @NotBlank(message = "El distrito es obligatorio")
    @Schema(description = "Nombre del catálogo maestro de Distritos", example = "Tecoluca")
    private String nombreDistrito;

    @NotBlank(message = "La dirección complementaria es obligatoria")
    @Schema(description = "Dirección complementaria exacta (Colonia, calle, pasaje, casa)", example = "Comunidad La Sabana")
    private String direccionComplementaria;
}
