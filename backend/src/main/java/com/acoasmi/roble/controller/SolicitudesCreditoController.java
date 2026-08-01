package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.SolicitudesCreditoRequestDTO;
import com.acoasmi.roble.dto.response.SolicitudesCreditoResponseDTO;
import com.acoasmi.roble.entity.SolicitudesCredito;
import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import com.acoasmi.roble.service.SolicitudesCreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-credito")
@Tag(name = "Solicitudes de Crédito", description = "Endpoints para la gestión e historial de solicitudes de crédito")
public class SolicitudesCreditoController
        extends AcoasmiController<SolicitudesCredito, SolicitudesCreditoRequestDTO, SolicitudesCreditoResponseDTO, Long> {

    private final SolicitudesCreditoService solicitudesCreditoService;

    public SolicitudesCreditoController(SolicitudesCreditoService solicitudesCreditoService) {
        super(solicitudesCreditoService, "Solicitud de Crédito");
        this.solicitudesCreditoService = solicitudesCreditoService;
    }

    @Operation(
            summary = "Obtener detalle por número de préstamo",
            description = "Retorna el detalle completo consolidado de una solicitud a partir de su número de préstamo asignado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada para el número proporcionado")
    })
    @GetMapping("/numero-prestamo/{numeroSolicitud}")
    public ResponseEntity<SolicitudesCreditoResponseDTO> getByNumeroSolicitud(
            @Parameter(description = "Número correlativo de préstamo (ej. SOLI-01-0001)", required = true)
            @PathVariable String numeroSolicitud) {
        return ResponseEntity.ok(solicitudesCreditoService.obtenerPorNumeroSolicitud(numeroSolicitud));
    }

    @Operation(
            summary = "Listar solicitudes paginadas por estado",
            description = "Filtra la lista de solicitudes de crédito por su estado de procesamiento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente")
    })
    @GetMapping("/estado")
    public ResponseEntity<Page<SolicitudesCreditoResponseDTO>> getByEstado(
            @Parameter(description = "Estado de la solicitud (ej. RECIBIDA, EN_REVISION, APROBADO)", required = true)
            @RequestParam EstadoSolicitudCredito estadoActual,
            Pageable pageable) {
        return ResponseEntity.ok(solicitudesCreditoService.listarPorEstadoActualSolicitud(estadoActual, pageable));
    }

    @Operation(
            summary = "Listar solicitudes asignadas a un asesor",
            description = "Obtiene todas las solicitudes de crédito asociadas al nombre de usuario de un asesor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes devuelta exitosamente")
    })
    @GetMapping("/asesor/{usuarioAsesor}")
    public ResponseEntity<List<SolicitudesCreditoResponseDTO>> getByAsesor(
            @Parameter(description = "Nombre de usuario del asesor", required = true)
            @PathVariable String usuarioAsesor) {
        return ResponseEntity.ok(solicitudesCreditoService.listarPorAsesor(usuarioAsesor));
    }
}