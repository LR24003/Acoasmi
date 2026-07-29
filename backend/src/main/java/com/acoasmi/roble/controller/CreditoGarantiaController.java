package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.CreditoGarantiasRequestDTO;
import com.acoasmi.roble.dto.response.CreditoGarantiasResponseDTO;
import com.acoasmi.roble.entity.CreditoGarantias;
import com.acoasmi.roble.service.CreditoGarantiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos-garantias")
@Tag(name = "Garantías de Crédito", description = "Endpoints para la gestión e inspección de garantías asociadas a solicitudes de crédito")
public class CreditoGarantiaController extends AcoasmiController<CreditoGarantias,
        CreditoGarantiasRequestDTO, CreditoGarantiasResponseDTO, Long> {

    private final CreditoGarantiaService creditoGarantiaService;

    public CreditoGarantiaController(CreditoGarantiaService creditoGarantiaService) {
        super(creditoGarantiaService, "Garantías de Credito");
        this.creditoGarantiaService = creditoGarantiaService;
    }

    @GetMapping("/buscar/tipo")
    @Operation(
            summary = "Filtrar por tipo de garantía",
            description = "Obtiene una lista de garantías que coincidan o contengan el tipo especificado (ej. HIPOTECARIA, FIADOR)."
    )
    public ResponseEntity<List<CreditoGarantiasResponseDTO>> obtenerPorTipoGarantia(@RequestParam String tipoGarantia) {
        return ResponseEntity.ok(creditoGarantiaService.obtenerPorTipoGarantia(tipoGarantia));
    }

    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Cambiar estado de una garantía",
            description = "Permite activar o desactivar explícitamente una garantía."
    )
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        creditoGarantiaService.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }
}
