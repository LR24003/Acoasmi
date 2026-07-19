package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.AsociadosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadosResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.service.AsociadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asociados")
@Tag(name = "Gestión de Asociados", description = "Endpoints específicos para la administración, búsquedas y control de asociados")
public class AsociadosController extends AcoasmiController<Asociados, AsociadosRequestDTO,
        AsociadosResponseDTO, Long> {

    private final AsociadosService asociadosService;

    public AsociadosController(AsociadosService asociadosService) {
        super(asociadosService, "Asociados");
        this.asociadosService = asociadosService;
    }

    @GetMapping("/buscar/nombre-completo")
    @Operation(
            summary = "Buscar por por nombres o apellidos del asociado",
            description = "Retorna la información del asociado utilizando su nombre parcial."
    )
    public ResponseEntity<List<AsociadosResponseDTO>> getByNombreCompletoAsociado(@RequestParam String nombreCompleto){
        return ResponseEntity.ok(asociadosService.getByNombreCompletoAsociado(nombreCompleto));
    }


    @GetMapping("/buscar/numero/asociado/{numero}")
    @Operation(
            summary = "Buscar por número de asociado",
            description = "Retorna la información del asociado utilizando su código correlativo único."
    )
    public ResponseEntity<AsociadosResponseDTO> getByNumeroAsociado(@PathVariable Integer numero) {
        return ResponseEntity.ok(asociadosService.getByNumeroAsociado(numero));
    }

    @GetMapping("/buscar/documento/{documento}")
    @Operation(
            summary = "Buscar por numero de documento",
            description = "Permite ubicar a un asociado mediante su número único de identidad."
    )
    public ResponseEntity<AsociadosResponseDTO> getByNumeroDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(asociadosService.getByNumeroDocumento(documento));
    }

    @GetMapping("/buscar/nit/{nit}")
    @Operation(
            summary = "Buscar por NIT",
            description = "Busca al asociado a través de su identificación tributaria."
    )
    public ResponseEntity<AsociadosResponseDTO> getByNit(@PathVariable String nit) {
        return ResponseEntity.ok(asociadosService.getByNit(nit));
    }

    @GetMapping("/buscar/email/{email}")
    @Operation(
            summary = "Buscar por correo electrónico",
            description = "Encuentra al asociado mediante su dirección de email registrada."
    )
    public ResponseEntity<AsociadosResponseDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(asociadosService.getByEmail(email));
    }

    @GetMapping("/filtrar/estado")
    @Operation(
            summary = "Listar filtrado por estado",
            description = "Retorna un listado paginado de asociados activos (true) o inactivos (false)."
    )
    public ResponseEntity<Page<AsociadosResponseDTO>> getByEstado(@RequestParam Boolean estado, Pageable pageable) {
        return ResponseEntity.ok(asociadosService.getByEstado(estado, pageable));
    }

    @GetMapping("/buscar/global")
    @Operation(
            summary = "Buscador predictivo global",
            description = "Filtra asociados de forma paginada que coincidan parcialmente con nombres, apellidos o número de documento."
    )
    public ResponseEntity<Page<AsociadosResponseDTO>> buscarGlobal(@RequestParam String filtro, Pageable pageable) {
        return ResponseEntity.ok(asociadosService.buscarGlobal(filtro, pageable));
    }
}