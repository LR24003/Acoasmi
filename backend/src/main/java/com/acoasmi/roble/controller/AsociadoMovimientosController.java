package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.AsociadoMovimientosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoMovimientosResponseDTO;
import com.acoasmi.roble.dto.response.ComprobanteMovimientoDTO;
import com.acoasmi.roble.dto.response.DetalleMovimientoEstadoCuentaDTO;
import com.acoasmi.roble.dto.response.EstadoCuentaResponseDTO;
import com.acoasmi.roble.entity.AsociadoMovimientos;
import com.acoasmi.roble.service.AsociadoMovimientosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asociado-movimientos")
@Tag(name = "Movimientos Monetarios", description = "Endpoints para la ejecución de depósitos, retiros y emisión/reimpresión de comprobantes de caja.")
public class AsociadoMovimientosController extends AcoasmiController<AsociadoMovimientos,
        AsociadoMovimientosRequestDTO, AsociadoMovimientosResponseDTO, Long> {

    private final AsociadoMovimientosService movimientosService;

    public AsociadoMovimientosController(AsociadoMovimientosService movimientosService) {
        super(movimientosService, "Movimientos Monetarios");
        this.movimientosService = movimientosService;
    }

    @GetMapping("/comprobantes-apertura/{numeroAsociado}")
    @Operation(
            summary = "Obtener tiquetes de apertura por número de asociado",
            description = "Recupera los comprobantes de apertura inicial de las cuentas pasivas generadas durante la afiliación del asociado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobantes de apertura encontrados exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontraron cuentas o aperturas para el número de asociado especificado.")
    })
    public ResponseEntity<List<ComprobanteMovimientoDTO>> obtenerComprobantesApertura(
            @Parameter(description = "Número correlativo institucional del asociado", example = "1001")
            @PathVariable Integer numeroAsociado) {
        return ResponseEntity.ok(movimientosService.obtenerComprobantesAperturaPorAsociado(numeroAsociado));
    }

    @GetMapping("/comprobante/{idMovimiento}")
    @Operation(
            summary = "Obtener/Reimprimir comprobante por ID de movimiento",
            description = "Recupera la información formateada de un movimiento financiero en caja para su reimpresión en tiquete."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobante recuperado exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró el movimiento con el ID proporcionado.")
    })
    public ResponseEntity<ComprobanteMovimientoDTO> obtenerComprobantePorId(
            @Parameter(description = "ID único del movimiento registrado", example = "45")
            @PathVariable Long idMovimiento) {
        ComprobanteMovimientoDTO comprobante = movimientosService.obtenerComprobantePorId(idMovimiento);
        return ResponseEntity.ok(comprobante);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(
            summary = "Obtener historial completo de una cuenta",
            description = "Recupera la cartola/historial completo de movimientos asociados a un número de cuenta específico."
    )
    public ResponseEntity<List<AsociadoMovimientosResponseDTO>> consultarHistorial(
            @Parameter(description = "Número único institucional de la cuenta", example = "1011-1001-1")
            @PathVariable String numeroCuenta) {
        List<AsociadoMovimientosResponseDTO> historial = movimientosService.obtenerHistorialPorNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/cuenta/{numeroCuenta}/paginado")
    @Operation(
            summary = "Obtener historial paginado de una cuenta",
            description = "Permite la navegación por bloques del historial de movimientos de una cuenta (ideal para estados de cuenta pesados)."
    )
    public ResponseEntity<Page<AsociadoMovimientosResponseDTO>> consultarHistorialPaginado(
            @Parameter(description = "Número único institucional de la cuenta", example = "1011-1001-1")
            @PathVariable String numeroCuenta,
            @PageableDefault() Pageable pageable) {
        Page<AsociadoMovimientosResponseDTO> historialPaginado = movimientosService.obtenerHistorialPorNumeroCuentaPaginado(numeroCuenta, pageable);
        return ResponseEntity.ok(historialPaginado);
    }

    @GetMapping("/asociado/{numeroAsociado}/estado-cuenta")
    public ResponseEntity<EstadoCuentaResponseDTO> obtenerEstadoCuenta(
            @PathVariable String numeroAsociado,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {

        return ResponseEntity.ok(movimientosService.obtenerEstadoCuenta(numeroAsociado, fechaInicio, fechaFin));
    }
}