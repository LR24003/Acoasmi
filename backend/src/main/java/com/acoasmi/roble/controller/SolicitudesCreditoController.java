package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.SolicitudesCreditoRequestDTO;
import com.acoasmi.roble.dto.response.SolicitudesCreditoResponseDTO;
import com.acoasmi.roble.entity.SolicitudesCredito;
import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import com.acoasmi.roble.service.SolicitudesCreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-credito")
@Tag(name = "Solicitudes de Crédito", description = "Endpoints para la gestión, flujo de aprobación e historial de solicitudes de crédito")
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
            @Parameter(description = "Estado de la solicitud (ej. EN_ANALISIS_ASESOR, REVISION_COMITE_CREDITO)", required = true)
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


    @Operation(
            summary = "Avanzar estado de aprobación",
            description = "Evalúa el monto y el estado actual de la solicitud para dictaminar automáticamente la siguiente etapa del flujo de aprobación y registrar la acción en el historial."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado avanzado exitosamente en el flujo de aprobación"),
            @ApiResponse(responseCode = "400", description = "La solicitud está en un estado terminal o los parámetros ingresados son inválidos"),
            @ApiResponse(responseCode = "404", description = "Solicitud o usuario responsable no encontrado")
    })
    @PatchMapping("/{id}/avanzar-estado")
    public ResponseEntity<SolicitudesCreditoResponseDTO> avanzarEstadoAprobacion(
            @Parameter(description = "ID de la solicitud de crédito", required = true)
            @PathVariable Long id,
            @RequestBody TransicionEstadoRequest request) {

        SolicitudesCreditoResponseDTO response = solicitudesCreditoService.avanzarEstadoAprobacion(
                id,
                request.usuarioResponsable(),
                request.observaciones()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechazar solicitud de crédito",
            description = "Cambia el estado de la solicitud a RECHAZADA y registra las observaciones o motivos del rechazo en el historial de aprobaciones."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud rechazada exitosamente"),
            @ApiResponse(responseCode = "400", description = "La solicitud ya fue rechazada o desembolsada previamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud o usuario responsable no encontrado")
    })
    @PatchMapping("/{id}/denegar")
    public ResponseEntity<SolicitudesCreditoResponseDTO> denegarSolicitud(
            @Parameter(description = "ID de la solicitud de crédito", required = true)
            @PathVariable Long id,
            @RequestBody denegarSolicitudRequest request) {

        SolicitudesCreditoResponseDTO response = solicitudesCreditoService.denegarSolicitud(
                id,
                request.usuarioResponsable(),
                request.observaciones()
        );
        return ResponseEntity.ok(response);
    }

    @Schema(description = "Estructura para solicitar la transición o avance de estado en el flujo de crédito")
    public record TransicionEstadoRequest(
            @Schema(description = "Nombre de usuario del responsable de la decisión", example = "jdoe")
            @NotBlank(message = "El usuario responsable es obligatorio")
            String usuarioResponsable,

            @Schema(description = "Observaciones o recomendaciones del nivel evaluador", example = "Cumple con las garantías requeridas.")
            String observaciones
    ) {}

    @Schema(description = "Estructura para solicitar el rechazo de una solicitud de crédito")
    public record denegarSolicitudRequest(
            @Schema(description = "Nombre de usuario del responsable de la decisión", example = "jdoe")
            @NotBlank(message = "El usuario responsable es obligatorio")
            String usuarioResponsable,

            @Schema(description = "Motivo detallado por el cual se rechaza la solicitud", example = "Capacidad de pago insuficiente según análisis DTI.")
            @NotBlank(message = "El motivo de rechazo es obligatorio")
            String observaciones
    ) {}
}