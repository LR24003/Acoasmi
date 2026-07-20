package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.AsociadoCuentasRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoCuentasResponseDTO;
import com.acoasmi.roble.entity.AsociadoCuentas;
import com.acoasmi.roble.service.AsociadoCuentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asociado-cuentas")
@Tag(name = "Cuentas de Asociados", description = "Endpoints para la gestión y consulta de las cuentas pasivas de los asociados")
public class AsociadoCuentasController extends AcoasmiController<AsociadoCuentas,
        AsociadoCuentasRequestDTO, AsociadoCuentasResponseDTO, Long> {

    private final AsociadoCuentasService asociadoCuentasService;

    public AsociadoCuentasController(AsociadoCuentasService asociadoCuentasService) {
        super(asociadoCuentasService, "Cuentas Asociados");
        this.asociadoCuentasService = asociadoCuentasService;
    }

    @PostMapping("/nueva-cuenta")
    @Operation(
            summary = "Crea una nueva cuenta de un asociado",
            description = "Retorna la nueva cuenta creada")
    public ResponseEntity<AsociadoCuentasResponseDTO> crearCuenta(@RequestBody AsociadoCuentasRequestDTO requestDto) {
        AsociadoCuentasResponseDTO nuevaCuenta = asociadoCuentasService.create(requestDto);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(
            summary = "Obtener la cuenta de un asociado por número de cuenta",
            description = "Retorna una cuenta pasiva específica del asociado")
    public ResponseEntity<AsociadoCuentasResponseDTO> obtenerPorNumeroCuenta(@PathVariable String numeroCuenta) {
        AsociadoCuentasResponseDTO cuenta = asociadoCuentasService.getByNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/asociado/{numeroAsociado}")
    @Operation(
            summary = "Obtener las cuentas de un asociado por número de asociado",
            description = "Retorna las cuentas pasivas vinculadas al asociado")
    public ResponseEntity<List<AsociadoCuentasResponseDTO>> obtenerPorNumeroAsociado(@PathVariable Integer numeroAsociado) {
        List<AsociadoCuentasResponseDTO> cuentas = asociadoCuentasService.getByNumeroAsociado(numeroAsociado);
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/asociado/{numeroAsociado}/plazo/{plazoDias}")
    @Operation(
            summary = "Obtener las cuentas de un asociado por el número de días plazo",
            description = "Retorna una lista con las cuentas pasivas vinculadas al asociado filtradas por un plazo específico en días."
    )
    public ResponseEntity<List<AsociadoCuentasResponseDTO>> obtenerCuentasPorAsociadoYPlazo(
            @PathVariable
            @Parameter(description = "Número único y correlativo del asociado", example = "1001")
            Integer numeroAsociado,

            @PathVariable
            @Parameter(description = "Plazo de retención en días (Ej: 180 días, 360 días)", example = "360 días")
            String plazoDias) {
        List<AsociadoCuentasResponseDTO> cuentas = asociadoCuentasService.obtenerCuentasPorAsociadoYPlazo(numeroAsociado, plazoDias);
        return ResponseEntity.ok(cuentas);
    }

}
