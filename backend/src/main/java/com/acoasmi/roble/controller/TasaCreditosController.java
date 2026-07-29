package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.TasaCreditosRequestDTO;
import com.acoasmi.roble.dto.response.TasaCreditosResponseDTO;
import com.acoasmi.roble.entity.TasasCreditos;
import com.acoasmi.roble.service.TasaCreditosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/creditos-tasas")
@Tag(name = "Tasas de Crédito", description = "Endpoints para la gestión de las líneas de crédito y sus tasas de interés de referencia")
public class TasaCreditosController extends AcoasmiController<TasasCreditos, TasaCreditosRequestDTO,
        TasaCreditosResponseDTO, Long>  {

    private final TasaCreditosService tasaCreditosService;

    public TasaCreditosController(TasaCreditosService tasaCreditoService) {
        super(tasaCreditoService, "Tasa de Créditos");
        this.tasaCreditosService = tasaCreditoService;
    }


    @GetMapping("/buscar/producto")
    @Operation(
            summary = "Buscar tasas por nombre de producto",
            description = "Obtiene las tasas cuyo nombre de producto coincida o contenga el texto buscado."
    )
    public ResponseEntity<List<TasaCreditosResponseDTO>> buscarPorNombreProducto(@RequestParam String nombreProducto) {
        return ResponseEntity.ok(tasaCreditosService.buscarPorNombreProducto(nombreProducto));
    }

    @GetMapping("/buscar/filtro")
    @Operation(
            summary = "Buscar tasas por nombre de producto y frecuencia de pago",
            description = "Filtra tasas por coincidencia parcial de nombre de producto y coincidencia exacta de frecuencia de pago."
    )
    public ResponseEntity<List<TasaCreditosResponseDTO>> buscarPorNombreYFrecuenciasPago(
            @RequestParam String nombreProducto,
            @RequestParam Collection<String> frecuenciasPago) {
        return ResponseEntity.ok(tasaCreditosService.buscarPorNombreYFrecuencia(nombreProducto, frecuenciasPago));
    }


    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Cambiar estado de una tasa de crédito",
            description = "Permite activar o desactivar explícitamente una línea de crédito según su ID."
    )
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        tasaCreditosService.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }

}
