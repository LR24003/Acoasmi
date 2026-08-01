package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.HistorialAprobacionesRequestDTO;
import com.acoasmi.roble.dto.response.HistorialAprobacionesResponseDTO;
import com.acoasmi.roble.entity.HistorialAprobaciones;
import com.acoasmi.roble.service.HistorialAprobacionesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resoluciones-credito")
@Tag(name = "Historial de Aprobaciones", description = "Endpoints para la gestión y consulta de evaluaciones e historial de solicitudes de crédito")
public class HistorialAprobacionesController extends AcoasmiController<HistorialAprobaciones,
        HistorialAprobacionesRequestDTO, HistorialAprobacionesResponseDTO, Long> {

    private final HistorialAprobacionesService historialAprobacionesService;

    public HistorialAprobacionesController(HistorialAprobacionesService historialAprobacionesService) {
        super(historialAprobacionesService, "Resoluciones de Crédito");
        this.historialAprobacionesService = historialAprobacionesService;
    }

    @PostMapping("/evaluar")
    @Operation(
            summary = "Registrar dictamen/evaluación",
            description = "Registra una decisión sobre la solicitud (Aprobado, Rechazado, etc.) y actualiza su estado actual."
    )
    public ResponseEntity<HistorialAprobacionesResponseDTO> registrarEvaluacion(
            @Valid @RequestBody HistorialAprobacionesRequestDTO requestDto) {
        HistorialAprobacionesResponseDTO response = historialAprobacionesService.registrarEvaluacion(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/solicitud/{numeroSolicitud}")
    @Operation(
            summary = "Consultar historial por número de solicitud",
            description = "Obtiene el historial completo de dictámenes de una solicitud ordenado cronológicamente."
    )
    public ResponseEntity<List<HistorialAprobacionesResponseDTO>> obtenerPorNumeroSolicitud(
            @PathVariable String numeroSolicitud) {
        List<HistorialAprobacionesResponseDTO> historial = historialAprobacionesService.obtenerHistorialPorNumeroSolicitud(numeroSolicitud);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/asociado/{numeroAsociado}")
    @Operation(
            summary = "Consultar historial por número de asociado",
            description = "Obtiene el historial de evaluaciones de todas las solicitudes pertenecientes a un asociado."
    )
    public ResponseEntity<List<HistorialAprobacionesResponseDTO>> obtenerPorNumeroAsociado(
            @PathVariable Integer numeroAsociado) {
        List<HistorialAprobacionesResponseDTO> historial = historialAprobacionesService.obtenerHistorialPorNumeroAsociado(numeroAsociado);
        return ResponseEntity.ok(historial);
    }
}
