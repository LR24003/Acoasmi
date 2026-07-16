package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que expone la información detallada de un Asociado con sus catálogos resueltos en nombres")
public class AsociadosResponseDTO {

    @Schema(description = "ID interno del asociado en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Número correlativo único del asociado", example = "1850")
    private Integer numeroAsociado;

    @Schema(description = "Nombres completos del asociado", example = "José Alfredo")
    private String nombres;

    @Schema(description = "Apellidos completos del asociado", example = "López Rivera")
    private String apellidos;

    @Schema(description = "Tipo de documento de identidad", example = "DUI")
    private String tipoDocumento;

    @Schema(description = "Número de documento de identidad", example = "01234567-8")
    private String numeroDocumento;

    @Schema(description = "Número de Identificación Tributaria", example = "0614-150795-101-4")
    private String nit;

    @Schema(description = "Número de Registro de Contribuyente", example = "N/A")
    private String nrc;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de nacimiento", example = "16/01/1993")
    private LocalDate fechaNacimiento;

    @Schema(description = "Edad del asociado", example = "33")
    private Integer edad;

    @Schema(description = "Descripción de la actividad económica del asociado", example = "Elaboración de tortillas")
    private String actividadEconomica;

    @Schema(description = "Teléfono de contacto", example = "2255-0000")
    private String telefono;

    @Schema(description = "Correo electrónico", example = "josepalfred7@gmail.com")
    private String email;

    @Schema(description = "Nombre del departamento de residencia", example = "San Vicente")
    private String departamento;

    @Schema(description = "Nombre del municipio de residencia", example = "San Vicente Sur")
    private String municipio;

    @Schema(description = "Nombre del distrito de residencia", example = "Tecoluca")
    private String distrito;

    @Schema(description = "Dirección residencial exacta", example = "Comunidad La Sabana")
    private String direccionComplementaria;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(description = "Hora y fecha del registro del asociado", example = "15/07/2026 12:30")
    private LocalDateTime fechaIngreso;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha y hora de retiro (null si está activo)", example = "null")
    private LocalDateTime fechaRetiro;
    
    @Schema(description = "Estado actual del asociado", example = "true")
    private Boolean estado;
}