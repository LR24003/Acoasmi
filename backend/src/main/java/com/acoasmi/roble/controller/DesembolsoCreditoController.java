package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.DesembolsoCreditoRequestDTO;
import com.acoasmi.roble.dto.response.DesembolsoCreditoResponseDTO;
import com.acoasmi.roble.entity.DesembolsoCredito;
import com.acoasmi.roble.service.DesembolsoCreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/desembolsos-credito")
@Tag(name = "Desembolso de Créditos", description = "API para la gestión y consulta de desembolsos de créditos, deducciones y facturación")
public class DesembolsoCreditoController
        extends AcoasmiController<DesembolsoCredito, DesembolsoCreditoRequestDTO, DesembolsoCreditoResponseDTO, Long> {

    private final DesembolsoCreditoService desembolsoService;

    public DesembolsoCreditoController(DesembolsoCreditoService desembolsoService) {
        super(desembolsoService, "Desembolsos Creditos");
        this.desembolsoService = desembolsoService;
    }

    @GetMapping("/numero-desembolso/{numeroDesembolso}")
    @Operation(summary = "Obtener por número de desembolso", description = "Consulta un desembolso a partir de su número o correlativo de desembolso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desembolso encontrado"),
            @ApiResponse(responseCode = "404", description = "Desembolso no encontrado")
    })
    public ResponseEntity<DesembolsoCreditoResponseDTO> obtenerPorNumeroDesembolso(
            @Parameter(description = "Número o correlativo de desembolso", required = true)
            @PathVariable String numeroDesembolso) {
        DesembolsoCreditoResponseDTO response = desembolsoService.obtenerPorNumeroDesembolso(numeroDesembolso);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prestamo/{numeroPrestamo}")
    @Operation(summary = "Listar desembolsos por préstamo", description = "Obtiene el historial de desembolsos asociados a un número de préstamo específico.")
    @ApiResponse(responseCode = "200", description = "Lista de desembolsos recuperada exitosamente")
    public ResponseEntity<List<DesembolsoCreditoResponseDTO>> obtenerPorNumeroPrestamo(
            @Parameter(description = "Número de préstamo", required = true)
            @PathVariable String numeroPrestamo) {
        List<DesembolsoCreditoResponseDTO> response = desembolsoService.obtenerPorNumeroPrestamo(numeroPrestamo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/asociado/{numeroAsociado}")
    @Operation(summary = "Listar desembolsos por asociado", description = "Obtiene todos los desembolsos realizados a un número de asociado.")
    @ApiResponse(responseCode = "200", description = "Lista de desembolsos recuperada exitosamente")
    public ResponseEntity<List<DesembolsoCreditoResponseDTO>> obtenerPorNumeroAsociado(
            @Parameter(description = "Número de asociado", required = true)
            @PathVariable Integer numeroAsociado) {
        List<DesembolsoCreditoResponseDTO> response = desembolsoService.obtenerPorNumeroAsociado(numeroAsociado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rango-fechas")
    @Operation(summary = "Consultar desembolsos por rango de fechas", description = "Filtra los desembolsos realizados en un periodo determinado.")
    @ApiResponse(responseCode = "200", description = "Lista de desembolsos en el rango recuperada exitosamente")
    public ResponseEntity<List<DesembolsoCreditoResponseDTO>> obtenerPorRangoFechas(
            @Parameter(description = "Fecha de inicio (ISO format: DD-MM-YYYY)", example = "01-01-2026")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,

            @Parameter(description = "Fecha fin (ISO format: DD-MM-YYYY)", example = "31-08-2026")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<DesembolsoCreditoResponseDTO> response = desembolsoService.obtenerPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(response);
    }
}