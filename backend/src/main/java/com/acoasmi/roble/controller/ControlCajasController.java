package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.CajaAperturaRequestDTO;
import com.acoasmi.roble.dto.request.CajaCierreRequestDTO;
import com.acoasmi.roble.dto.response.ControlCajasResponseDTO;
import com.acoasmi.roble.entity.ControlCajas;
import com.acoasmi.roble.service.CatalogoCuentasService;
import com.acoasmi.roble.service.ControlCajasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/control-cajas")
@Tag(name = "Control de Cajas", description = "Endpoints para la gestión, apertura, arqueo y cierre de caja")
public class ControlCajasController extends AcoasmiController<ControlCajas, CajaAperturaRequestDTO,
        ControlCajasResponseDTO, Long> {

    private final ControlCajasService controlCajasService;

    public ControlCajasController(ControlCajasService controlCajasService) {
        super(controlCajasService, "Control de Cajas");
        this.controlCajasService = controlCajasService;
    }

    @PostMapping("/apertura")
    @Operation(
            summary = "Apertura de caja",
            description = "Inicia una nueva sesión operativa de caja para un cajero con un fondo fijo inicial."
    )
    public ResponseEntity<ControlCajasResponseDTO> abrirCaja(@Valid @RequestBody CajaAperturaRequestDTO aperturaDto) {
        ControlCajasResponseDTO response = controlCajasService.create(aperturaDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/arqueo-intermedio")
    @Operation(
            summary = "Arqueo intermedio",
            description = "Realiza un conteo físico intermedio de la caja en cualquier momento de la jornada sin cerrar la sesión."
    )
    public ResponseEntity<ControlCajasResponseDTO> realizarArqueoIntermedio(
            @PathVariable Long id,
            @Valid @RequestBody CajaCierreRequestDTO arqueoDto) {
        ControlCajasResponseDTO response = controlCajasService.realizarArqueoIntermedio(id, arqueoDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cierre")
    @Operation(
            summary = "Cierre de caja",
            description = "Efectúa el cierre definitivo de la sesión de caja guardando el arqueo físico final."
    )
    public ResponseEntity<ControlCajasResponseDTO> cerrarCaja(
            @PathVariable Long id,
            @Valid @RequestBody CajaCierreRequestDTO cierreDto) {
        ControlCajasResponseDTO response = controlCajasService.cerrarCaja(id, cierreDto);
        return ResponseEntity.ok(response);
    }

}
