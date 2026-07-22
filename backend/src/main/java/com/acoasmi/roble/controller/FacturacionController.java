package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.FacturacionRequestDTO;
import com.acoasmi.roble.dto.response.FacturacionResponseDTO;
import com.acoasmi.roble.entity.Facturas;
import com.acoasmi.roble.service.FacturacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/facturacion-electronica")
@Tag(name = "Facturación Electrónica", description = "Endpoints especializados para la emisión y consulta de Documentos Tributarios Electrónicos (DTE)")
public class FacturacionController extends AcoasmiController<Facturas,
        FacturacionRequestDTO, FacturacionResponseDTO, Long> {

    private final FacturacionService facturacionService;

    public FacturacionController(FacturacionService facturacionService) {
        super(facturacionService, "Facturacion Electronica");
        this.facturacionService = facturacionService;
    }


    @GetMapping("/uuid/{uuid}")
    @Operation(
            summary = "Obtener factura por UUID",
            description = "Recupera los detalles de un DTE específico utilizando su Código de Generación UUID único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna factura con el UUID especificado")
    })
    public ResponseEntity<FacturacionResponseDTO> obtenerPorUuid(
            @Parameter(description = "Código de generación UUID del DTE a buscar", required = true)
            @PathVariable UUID uuid) {
        return ResponseEntity.ok(facturacionService.obtenerPorUuid(uuid));
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(
            summary = "Listar facturas por usuario cajero",
            description = "Retorna el historial de facturas emitidas por un usuario específico de manera paginada."
    )
    @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente")
    public ResponseEntity<Page<FacturacionResponseDTO>> listarPorCajaUsuario(
            @Parameter(description = "Nombre de usuario del cajero", required = true)
            @PathVariable String usuario,
            Pageable pageable) {
        return ResponseEntity.ok(facturacionService.getByCajaUsuario(usuario, pageable));
    }

    @GetMapping("/asociado/{numeroAsociado}")
    @Operation(
            summary = "Listar facturas por número de asociado",
            description = "Retorna el historial de facturas emitidas a nombre de un asociado institucional específico de manera paginada."
    )
    @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente")
    public ResponseEntity<Page<FacturacionResponseDTO>> listarPorNumeroAsociado(
            @Parameter(description = "Número institucional del asociado", required = true)
            @PathVariable Integer numeroAsociado,
            Pageable pageable) {
        return ResponseEntity.ok(facturacionService.getByAsociadoNumeroAsociado(numeroAsociado, pageable));
    }
}