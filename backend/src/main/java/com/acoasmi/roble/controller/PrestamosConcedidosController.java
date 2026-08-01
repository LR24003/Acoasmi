package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.GenerarPlanPagosRequestDTO;
import com.acoasmi.roble.dto.request.PrestamosConcedidosRequestDTO;
import com.acoasmi.roble.dto.request.SimularPlanPagosRequestDTO;
import com.acoasmi.roble.dto.response.PlanPagosResponseDTO;
import com.acoasmi.roble.dto.response.PrestamosConcedidosResponseDTO;
import com.acoasmi.roble.entity.PrestamosConcedidos;
import com.acoasmi.roble.service.PrestamoConcedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos-concedidos")
@Tag(name = "Préstamos Concedidos", description = "Endpoints para la gestión, consulta y planes de pago de préstamos concedidos.")
public class PrestamosConcedidosController extends AcoasmiController<PrestamosConcedidos,
        PrestamosConcedidosRequestDTO, PrestamosConcedidosResponseDTO, Long> {

    private final PrestamoConcedidoService prestamoConcedidoService;

    public PrestamosConcedidosController(PrestamoConcedidoService prestamoConcedidoService) {
        super(prestamoConcedidoService, "Préstamos Concedidos");
        this.prestamoConcedidoService = prestamoConcedidoService;
    }


    @Operation(summary = "Obtener préstamo por número de préstamo", description = "Recupera la información completa de un préstamo concedido utilizando su código/número único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @GetMapping("/numero/{numeroPrestamo}")
    public ResponseEntity<PrestamosConcedidosResponseDTO> obtenerPorNumeroPrestamo(
            @Parameter(description = "Número único del préstamo", required = true)
            @PathVariable String numeroPrestamo) {
        return ResponseEntity.ok(prestamoConcedidoService.obtenerPorNumeroPrestamo(numeroPrestamo));
    }

    @Operation(summary = "Obtener préstamos por número de asociado", description = "Obtiene la lista de préstamos concedidos pertenecientes a un asociado específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamos recuperados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asociado no encontrado o sin préstamos")
    })
    @GetMapping("/asociado/{numeroAsociado}")
    public ResponseEntity<List<PrestamosConcedidosResponseDTO>> obtenerPorNumeroAsociado(
            @Parameter(description = "Número del asociado", required = true)
            @PathVariable String numeroAsociado) {
        return ResponseEntity.ok(prestamoConcedidoService.obtenerPorNumeroAsociado(numeroAsociado));
    }


    @Operation(summary = "Simular un plan de pagos", description = "Calcula proyectadamente la tabla de amortización con sus cuotas e intereses sin guardar nada en base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulación realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de simulación inválidos")
    })
    @PostMapping("/simular-plan-pagos")
    public ResponseEntity<PlanPagosResponseDTO> simularPlanPagos(
            @Valid @RequestBody SimularPlanPagosRequestDTO request) {
        return ResponseEntity.ok(prestamoConcedidoService.simularPlanPagos(request));
    }

    @Operation(summary = "Generar y guardar plan de pagos", description = "Genera las cuotas proyectadas para un préstamo concedido y las almacena en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan de pagos generado y almacenado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de la solicitud inválidos")
    })
    @PostMapping("/plan-pagos/generar")
    public ResponseEntity<PlanPagosResponseDTO> generarYGuardarPlanPagos(
            @Valid @RequestBody GenerarPlanPagosRequestDTO request) {
        return new ResponseEntity<>(prestamoConcedidoService.generarYGuardarPlanPagos(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener el plan de pagos de un préstamo", description = "Consulta la tabla de amortización/cuotas generadas anteriormente para un préstamo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de pagos obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo o plan de pagos no encontrado")
    })
    @GetMapping("/plan-pagos/{numeroPrestamo}")
    public ResponseEntity<PlanPagosResponseDTO> obtenerPlanPagosPorNumeroPrestamo(
            @Parameter(description = "Número único del préstamo", required = true)
            @PathVariable String numeroPrestamo) {
        return ResponseEntity.ok(prestamoConcedidoService.obtenerPlanPagosPorNumeroPrestamo(numeroPrestamo));
    }
}